======================================
  قبول تلقائي جيني - Jeeny Auto Accept
======================================

طريقة فتح المشروع في Android Studio:
--------------------------------------
1. افتح Android Studio
2. اختر "Open" أو "Open an Existing Project"
3. اختر مجلد JeenyAutoAccept
4. انتظر Gradle Sync ينتهي
5. اضغط Run ▶ لبناء التطبيق على جهازك

طريقة تشغيل التطبيق:
----------------------
1. افتح التطبيق على موبايلك
2. اضغط "تفعيل خدمة الوصول" → ابحث عن "قبول تلقائي جيني" وفعّله
3. اضغط "تفعيل الظهور فوق التطبيقات" وامنحه الإذن
4. اضبط الإعدادات (المسافة والسعر) من زر الإعدادات
5. شغّل السويتش الكبير "تفعيل القبول التلقائي"
6. افتح تطبيق جيني - سيقبل الطلبات تلقائياً!

هيكل الملفات:
--------------
app/src/main/java/com/jeeny/autoaccept/
  - MainActivity.kt           → الشاشة الرئيسية مع زر التشغيل
  - JeenyAccessibilityService.kt → خدمة قراءة الشاشة والقبول التلقائي
  - SettingsActivity.kt       → إعدادات المسافة والسعر

app/src/main/res/
  - layout/activity_main.xml     → واجهة الشاشة الرئيسية
  - layout/activity_settings.xml → واجهة الإعدادات
  - xml/accessibility_service_config.xml → إعدادات خدمة الوصول
  - values/strings.xml           → النصوص
  - values/colors.xml            → الألوان

app/src/main/AndroidManifest.xml → ملف المانيفست والصلاحيات
======================================
