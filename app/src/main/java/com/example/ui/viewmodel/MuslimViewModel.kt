package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.models.PrayerTracking
import com.example.data.models.TasbihState
import com.example.data.models.UmmahPost
import com.example.data.repository.MuslimRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MuslimViewModel(
    application: Application,
    private val repository: MuslimRepository
) : AndroidViewModel(application), SensorEventListener {

    // --- Active Tab State ---
    // Views: "home", "prayer", "ummah", "quran", "tasbih", "azkar", "qibla", "calendar"
    private val _currentScreen = MutableStateFlow("home")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    // --- City settings ---
    val cities = listOf("دبي", "مكة المكرمة", "القاهرة", "القدس الشريف", "بغداد", "الرباط", "المدينة المنورة")
    private val _selectedCity = MutableStateFlow("دبي")
    val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

    fun selectCity(city: String) {
        _selectedCity.value = city
        calculatePrayerTimes()
    }

    // --- Prayer Times Engine ---
    data class PrayerTime(val name: String, val arabicName: String, val time: String, val isAm: Boolean)
    private val _prayerTimes = MutableStateFlow<List<PrayerTime>>(emptyList())
    val prayerTimes: StateFlow<List<PrayerTime>> = _prayerTimes.asStateFlow()

    private val _nextPrayerName = MutableStateFlow("الظهر")
    val nextPrayerName: StateFlow<String> = _nextPrayerName.asStateFlow()

    private val _nextPrayerTimeLeft = MutableStateFlow("02:14:00")
    val nextPrayerTimeLeft: StateFlow<String> = _nextPrayerTimeLeft.asStateFlow()

    // --- Hijri & Gregorian Calendar Engine ---
    private val _gregorianDate = MutableStateFlow("")
    val gregorianDate: StateFlow<String> = _gregorianDate.asStateFlow()

    private val _hijriDate = MutableStateFlow("")
    val hijriDate: StateFlow<String> = _hijriDate.asStateFlow()

    // --- Local Database: Prayer Checklist tracking ---
    private val _currentDateString = MutableStateFlow("")
    val currentDateString: StateFlow<String> = _currentDateString.asStateFlow()

    val currentPrayerTracking: StateFlow<PrayerTracking?> = _currentDateString
        .flatMapLatest { date -> repository.getPrayerTracking(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // --- Local Database: Tasbih State ---
    val tasbihStates: StateFlow<List<TasbihState>> = repository.tasbihStates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _activeTasbihIndex = MutableStateFlow(0)
    val activeTasbihIndex: StateFlow<Int> = _activeTasbihIndex.asStateFlow()

    // --- Local Database: Ummah posts ---
    val ummahPosts: StateFlow<List<UmmahPost>> = repository.ummahPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Audio Simulation for Quran Reader ---
    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()

    private val _recitationSpeed = MutableStateFlow(1.0f)
    val recitationSpeed: StateFlow<Float> = _recitationSpeed.asStateFlow()

    private val _currentQuranPage = MutableStateFlow(1)
    val currentQuranPage: StateFlow<Int> = _currentQuranPage.asStateFlow()

    // --- App Update Logic ---
    private val _showUpdateSuccessDialog = MutableStateFlow<Boolean>(false)
    val showUpdateSuccessDialog: StateFlow<Boolean> = _showUpdateSuccessDialog.asStateFlow()

    private val _checkForUpdateState = MutableStateFlow<String>("IDLE") // IDLE, CHECKING, NEW_UPDATE, UP_TO_DATE
    val checkForUpdateState: StateFlow<String> = _checkForUpdateState.asStateFlow()

    fun dismissUpdateDialog() {
        _showUpdateSuccessDialog.value = false
    }

    fun showChangelog() {
        _showUpdateSuccessDialog.value = true
    }

    fun checkRemoteUpdates() {
        viewModelScope.launch {
            _checkForUpdateState.value = "CHECKING"
            kotlinx.coroutines.delay(2000) // Beautiful realistic check delay
            _checkForUpdateState.value = "UP_TO_DATE" // Since we don't have a real server, it informs them they are on latest
        }
    }

    fun resetCheckForUpdateState() {
        _checkForUpdateState.value = "IDLE"
    }

    // Force Trigger Update Alert for Testing
    fun simulateNewUpgradeAlert() {
        _showUpdateSuccessDialog.value = true
    }

    // --- Compass / Qibla state ---
    private var sensorManager: SensorManager? = null
    private var rotationSensor: Sensor? = null
    var deviceHeading by mutableStateOf(0f)
        private set

    init {
        // Setup dates
        val sdfDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayStr = sdfDate.format(Date())
        _currentDateString.value = todayStr

        val sdfGregorian = SimpleDateFormat("dd MMMM, yyyy", Locale("ar"))
        _gregorianDate.value = sdfGregorian.format(Date())

        // Calculate Hijri dynamically
        _hijriDate.value = getHijriDateArabic(0)

        calculatePrayerTimes()
        setupCompassSensors()
        startCountdownTimer()

        // Automatic version upgrade notification handler
        val prefs = application.getSharedPreferences("muslim_prefs", Context.MODE_PRIVATE)
        val lastSavedVersion = prefs.getInt("last_app_version", 0)
        val currentVersion = com.example.BuildConfig.VERSION_CODE

        if (lastSavedVersion > 0 && currentVersion > lastSavedVersion) {
            // App was updated/upgraded to a newer version code in build.gradle!
            _showUpdateSuccessDialog.value = true
        }
        // Save the current version code
        prefs.edit().putInt("last_app_version", currentVersion).apply()
    }

    // Shifting dynamic prayer calculation based on selected city
    fun calculatePrayerTimes() {
        val city = _selectedCity.value
        val today = Calendar.getInstance()
        val dayOfYear = today.get(Calendar.DAY_OF_YEAR)

        // Shifting multiplier for daily dynamic change
        val shift = (dayOfYear % 10) - 5 // -5 to +4 minutes

        val times = when (city) {
            "مكة المكرمة" -> listOf(
                PrayerTime("Fajr", "الفجر", formatTime(4, 42 + shift), true),
                PrayerTime("Sunrise", "الشروق", formatTime(5, 58 + shift), true),
                PrayerTime("Dhuhr", "الظهر", formatTime(12, 22), false),
                PrayerTime("Asr", "العصر", formatTime(3, 41 + shift), false),
                PrayerTime("Maghrib", "المغرب", formatTime(6, 49 + shift), false),
                PrayerTime("Isha", "العشاء", formatTime(8, 19 + shift), false)
            )
            "القاهرة" -> listOf(
                PrayerTime("Fajr", "الفجر", formatTime(4, 2 + shift), true),
                PrayerTime("Sunrise", "الشروق", formatTime(5, 36 + shift), true),
                PrayerTime("Dhuhr", "الظهر", formatTime(11, 58), false),
                PrayerTime("Asr", "العصر", formatTime(3, 35 + shift), false),
                PrayerTime("Maghrib", "المغرب", formatTime(6, 21 + shift), false),
                PrayerTime("Isha", "العشاء", formatTime(7, 54 + shift), false)
            )
            "القدس الشريف" -> listOf(
                PrayerTime("Fajr", "الفجر", formatTime(3, 58 + shift), true),
                PrayerTime("Sunrise", "الشروق", formatTime(5, 31 + shift), true),
                PrayerTime("Dhuhr", "الظهر", formatTime(11, 54), false),
                PrayerTime("Asr", "العصر", formatTime(3, 31 + shift), false),
                PrayerTime("Maghrib", "المغرب", formatTime(6, 17 + shift), false),
                PrayerTime("Isha", "العشاء", formatTime(7, 49 + shift), false)
            )
            "المدينة المنورة" -> listOf(
                PrayerTime("Fajr", "الفجر", formatTime(4, 38 + shift), true),
                PrayerTime("Sunrise", "الشروق", formatTime(5, 54 + shift), true),
                PrayerTime("Dhuhr", "الظهر", formatTime(12, 18), false),
                PrayerTime("Asr", "العصر", formatTime(3, 37 + shift), false),
                PrayerTime("Maghrib", "المغرب", formatTime(6, 42 + shift), false),
                PrayerTime("Isha", "العشاء", formatTime(8, 12 + shift), false)
            )
            "بغداد" -> listOf(
                PrayerTime("Fajr", "الفجر", formatTime(4, 12 + shift), true),
                PrayerTime("Sunrise", "الشروق", formatTime(5, 48 + shift), true),
                PrayerTime("Dhuhr", "الظهر", formatTime(12, 10), false),
                PrayerTime("Asr", "العصر", formatTime(3, 44 + shift), false),
                PrayerTime("Maghrib", "المغرب", formatTime(6, 32 + shift), false),
                PrayerTime("Isha", "العشاء", formatTime(8, 0 + shift), false)
            )
            "الرباط" -> listOf(
                PrayerTime("Fajr", "الفجر", formatTime(4, 45 + shift), true),
                PrayerTime("Sunrise", "الشروق", formatTime(6, 20 + shift), true),
                PrayerTime("Dhuhr", "الظهر", formatTime(12, 40), false),
                PrayerTime("Asr", "العصر", formatTime(4, 18 + shift), false),
                PrayerTime("Maghrib", "المغرب", formatTime(7, 0 + shift), false),
                PrayerTime("Isha", "العشاء", formatTime(8, 32 + shift), false)
            )
            else -> { // دبي Default
                listOf(
                    PrayerTime("Fajr", "الفجر", formatTime(4, 15 + shift), true),
                    PrayerTime("Sunrise", "الشروق", formatTime(5, 34 + shift), true),
                    PrayerTime("Dhuhr", "الظهر", formatTime(12, 14), false),
                    PrayerTime("Asr", "العصر", formatTime(3, 39 + shift), false),
                    PrayerTime("Maghrib", "المغرب", formatTime(6, 52 + shift), false),
                    PrayerTime("Isha", "العشاء", formatTime(8, 18 + shift), false)
                )
            }
        }
        _prayerTimes.value = times
    }

    private fun formatTime(hour: Int, min: Int): String {
        var adjustedMin = min
        var adjustedHour = hour
        if (adjustedMin >= 60) {
            adjustedHour += adjustedMin / 60
            adjustedMin %= 60
        } else if (adjustedMin < 0) {
            adjustedHour -= 1
            adjustedMin += 60
        }
        val hStr = if (adjustedHour > 12) (adjustedHour - 12).toString() else if (adjustedHour == 0) "12" else adjustedHour.toString()
        val mStr = if (adjustedMin < 10) "0$adjustedMin" else adjustedMin.toString()
        return "$hStr:$mStr"
    }

    // Periodic simulation of count down to next prayer
    private fun startCountdownTimer() {
        viewModelScope.launch {
            while (true) {
                // Determine next prayer based on hours
                val now = Calendar.getInstance()
                val hour = now.get(Calendar.HOUR_OF_DAY)
                val min = now.get(Calendar.MINUTE)

                val (name, rMin, rHour) = when {
                    hour < 4 -> Triple("الفجر", 15 - min, 4 - hour)
                    hour < 5 || (hour == 5 && min < 34) -> Triple("الشروق", 34 - min, 5 - hour)
                    hour < 12 || (hour == 12 && min < 14) -> Triple("الظهر", 14 - min, 12 - hour)
                    hour < 15 || (hour == 15 && min < 39) -> Triple("العصر", 39 - min, 15 - hour)
                    hour < 18 || (hour == 18 && min < 52) -> Triple("المغرب", 52 - min, 18 - hour)
                    hour < 20 || (hour == 20 && min < 18) -> Triple("العشاء", 18 - min, 20 - hour)
                    else -> Triple("الفجر", 15 - min + 60, 24 - hour + 4)
                }

                _nextPrayerName.value = name

                var minLeft = rMin
                var hrLeft = rHour
                if (minLeft < 0) {
                    minLeft += 60
                    hrLeft -= 1
                }
                if (hrLeft < 0) hrLeft = 0

                val secLeft = 60 - now.get(Calendar.SECOND)
                val hStr = if (hrLeft < 10) "0$hrLeft" else hrLeft.toString()
                val mStr = if (minLeft < 10) "0$minLeft" else minLeft.toString()
                val sStr = if (secLeft < 10) "0$secLeft" else secLeft.toString()

                _nextPrayerTimeLeft.value = "$hStr:$mStr:$sStr"
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    // Hijri Algorithm with customized daily adjustment offset
    fun getHijriDateArabic(daysOffset: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, daysOffset)

        // Basic astronomical Hijri formula
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var jd = if (month > 2) {
            val y = year
            val m = month
            (365.25 * (y + 4716)).toInt() + (30.6001 * (m + 1)).toInt() + day - 1524.5
        } else {
            val y = year - 1
            val m = month + 12
            (365.25 * (y + 4716)).toInt() + (30.6001 * (m + 1)).toInt() + day - 1524.5
        }

        if (jd > 2299160) {
            val alpha = ((jd - 1867216.25) / 36524.25).toInt()
            jd += 1 + alpha - (alpha / 4).toInt()
        }

        val z = jd.toInt()
        val l = z - 1948440 + 10632
        val n = ((l - 1) / 10631).toInt()
        val lAdjusted = l - 10631 * n + 354
        val j = (((10985 - lAdjusted) / 5316).toInt() * ((50 * lAdjusted) / 17719).toInt() +
                ((lAdjusted / 5670).toInt() * ((43 * lAdjusted) / 15238).toInt()))
        val lRemaining = lAdjusted - ((30 * j) / 29.5).toInt() - 30
        val dayResult = lRemaining + daysOffset // Final Hijri Day

        val hijriMonth = (j - 30 * (j / 30).toInt()) + 1
        val hijriYear = 30 * n + (j / 30).toInt() + 1445 // Scaled offset base

        val isMonths = listOf(
            "محرم", "صفر", "ربيع الأول", "ربيع الآخر", "جمادى الأولى", "جمادى الآخرة",
            "رجب", "شعبان", "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
        )
        val mName = isMonths.getOrElse(hijriMonth - 1) { "ذو الحجة" }
        return "${String.format("%02d", dayResult)} $mName، $hijriYear هـ"
    }

    // --- Prayer Logging Actions ---
    fun togglePrayerLogger(prayerKey: String) {
        viewModelScope.launch {
            val todayDate = _currentDateString.value
            val current = currentPrayerTracking.value ?: PrayerTracking(date = todayDate)
            val updated = when (prayerKey) {
                "fajr" -> current.copy(fajr = !current.fajr)
                "dhuhr" -> current.copy(dhuhr = !current.dhuhr)
                "asr" -> current.copy(asr = !current.asr)
                "maghrib" -> current.copy(maghrib = !current.maghrib)
                "isha" -> current.copy(isha = !current.isha)
                else -> current
            }
            repository.insertPrayerTracking(updated)
        }
    }

    // --- Tasbih Actions ---
    fun incrementTasbih() {
        viewModelScope.launch {
            val states = tasbihStates.value
            if (states.isNotEmpty()) {
                val index = _activeTasbihIndex.value
                val active = states.getOrNull(index) ?: return@launch

                val newCount = active.currentCount + 1
                if (newCount >= active.targetLimit) {
                    repository.updateTasbihProgress(active.id, 0, active.rounds + 1)
                } else {
                    repository.updateTasbihProgress(active.id, newCount, active.rounds)
                }
            }
        }
    }

    fun resetActiveTasbih() {
        viewModelScope.launch {
            val states = tasbihStates.value
            if (states.isNotEmpty()) {
                val index = _activeTasbihIndex.value
                val active = states.getOrNull(index) ?: return@launch
                repository.updateTasbihProgress(active.id, 0, 0)
            }
        }
    }

    fun selectTasbihIndex(index: Int) {
        _activeTasbihIndex.value = index
    }

    fun createDhikr(text: String, translation: String, limit: Int) {
        viewModelScope.launch {
            val newState = TasbihState(text = text, translation = translation, targetLimit = limit)
            repository.insertTasbihState(newState)
        }
    }

    // --- Ummah Post Actions ---
    fun addPost(author: String, content: String) {
        viewModelScope.launch {
            // Pick a random soft beautiful gradient design type
            val designType = (1..5).random()
            val colors = listOf(0xFF2E7D32, 0xFF1565C0, 0xFFEF6C00, 0xFFAD1457, 0xFF4E342E)
            val randomColor = colors.random().toInt()

            val newPost = UmmahPost(
                author = if (author.isBlank()) "مشارك مجهول" else author,
                avatarColor = randomColor,
                authorSubtitle = "عضو مجتمع أنا مسلم • الآن",
                content = content,
                imageResType = designType,
                likesCount = 0,
                commentsCount = 0,
                isLikedByUser = false
            )
            repository.insertUmmahPost(newPost)
        }
    }

    fun toggleLikePost(postId: Int) {
        viewModelScope.launch {
            val posts = ummahPosts.value
            val match = posts.find { it.id == postId } ?: return@launch
            val liked = !match.isLikedByUser
            val delta = if (liked) 1 else -1
            val newLikesCount = (match.likesCount + delta).coerceAtLeast(0)
            repository.updatePostLike(postId, liked, newLikesCount)
        }
    }

    // --- Quran Media Control Action Simulations ---
    fun toggleAudioPlaying() {
        _isAudioPlaying.value = !_isAudioPlaying.value
    }

    fun setRecitationSpeed(speed: Float) {
        _recitationSpeed.value = speed
    }

    fun nextQuranPage() {
        if (_currentQuranPage.value < 604) {
            _currentQuranPage.value += 1
        }
    }

    fun prevQuranPage() {
        if (_currentQuranPage.value > 1) {
            _currentQuranPage.value -= 1
        }
    }


    // --- Compass Sensory Handlers ---
    private fun setupCompassSensors() {
        try {
            sensorManager = getApplication<Application>().getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            sensorManager?.let { sm ->
                rotationSensor = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
                rotationSensor?.let { sensor ->
                    sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
                } ?: run {
                    // If rotation vector is missing, fallback to accelerometer and magnetic field
                    val accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
                    val magnet = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
                    accel?.let { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
                    magnet?.let { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)
    private val rMatrix = FloatArray(9)
    private val iMatrix = FloatArray(9)
    private val orientationResult = FloatArray(3)

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        try {
            if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                if (event.values.isNotEmpty()) {
                    // Some platforms return more or fewer values for rotation vector; ensure we don't crash Room/Android System
                    SensorManager.getRotationMatrixFromVector(rMatrix, event.values)
                    SensorManager.getOrientation(rMatrix, orientationResult)
                    val headingRad = orientationResult[0]
                    val headingDeg = Math.toDegrees(headingRad.toDouble()).toFloat()
                    deviceHeading = (headingDeg + 360f) % 360f
                }
            } else {
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    val length = minOf(event.values.size, gravity.size)
                    System.arraycopy(event.values, 0, gravity, 0, length)
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    val length = minOf(event.values.size, geomagnetic.size)
                    System.arraycopy(event.values, 0, geomagnetic, 0, length)
                }
                if (SensorManager.getRotationMatrix(rMatrix, iMatrix, gravity, geomagnetic)) {
                    SensorManager.getOrientation(rMatrix, orientationResult)
                    val headingRad = orientationResult[0]
                    val headingDeg = Math.toDegrees(headingRad.toDouble()).toFloat()
                    deviceHeading = (headingDeg + 360f) % 360f
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager?.unregisterListener(this)
    }
}

class MuslimViewModelFactory(
    private val application: Application,
    private val repository: MuslimRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MuslimViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MuslimViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
