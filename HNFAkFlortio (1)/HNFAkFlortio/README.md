# HNFAk Flortio (ilk sürüm – arayüz iskeleti)

Kotlin + Jetpack Compose + Material 3 ile yazılmış, tek Activity'li, Türkçe arayüzlü flört uygulaması iskeleti.
**Bu sürümde backend yoktur.** Giriş, kayıt, şifre sıfırlama, profil kaydetme ve hesap silme çağrıları
açıkça "Sunucu bağlantısı henüz kurulmadı" hatasıyla sonuçlanır; hiçbir sahte kullanıcı, sohbet veya başarılı giriş üretilmez.

## Açma ve çalıştırma
1. Android Studio (Ladybug 2024.2 veya üstü) → **Open** → bu klasörü seç.
2. JDK 17 kullanıldığından emin ol (Settings → Build Tools → Gradle → Gradle JDK).
3. Gradle senkronizasyonu `gradle-wrapper.properties` içindeki Gradle 8.9'u kendisi indirir.
   (`gradlew` betikleri ZIP'te yok; Android Studio kullanıyorsan gerekmez. Terminalde gerekirse `gradle wrapper` çalıştır.)
4. Emülatör veya cihazda **Run ▶**. Birim testleri: `./gradlew test` (doğrulama kuralları).

Gereksinimler: minSdk 26, compileSdk/targetSdk 35, Kotlin 2.0.21, AGP 8.7.3, Compose BOM 2024.12.01.

## Debug "önizleme" düğmesi
Açılış ekranındaki **"Tasarımı önizle (hesapsız)"** düğmesi yalnızca debug derlemelerinde görünür.
Oturum açmadan Keşfet/Mesajlar/Profil/Ayarlar ekranlarını görmeni sağlar; oturum açılmış gibi davranmaz.

## Mimari
```
domain/        DataResult/AppError, modeller, repository ARAYÜZLERİ, saf doğrulama (Validators)
data/unconfigured/  Backend yokken kullanılan implementasyonlar (hepsi NotConfigured döner)
di/AppContainer     Bağımlılıkların tek toplandığı yer
ui/            Compose ekranları + ViewModel'ler (theme, components, navigation, her özellik kendi paketinde)
```
Durumlar açıkça modellenir: `SubmitState` (boşta/yükleniyor/başarılı/hatalı) ve `ListState` (yükleniyor/boş/hata/içerik).

## Firebase aşaması için yapılacaklar
1. `app/build.gradle.kts`'e Firebase BOM, `firebase-auth`, `firebase-firestore` ve `google-services` eklentisini ekle; `google-services.json` koy.
2. `AndroidManifest.xml`'e `INTERNET` iznini ekle (şu an bilinçli olarak yok).
3. `AuthRepository`, `ProfileRepository`, `DiscoverRepository`, `ConversationRepository`, `BlockRepository` için Firebase implementasyonlarını yaz.
4. `di/AppContainer.kt` içindeki `Unconfigured*` sınıflarını yenileriyle değiştir. UI ve ViewModel kodu değişmez.
5. `isBackendConfigured` değerini `true` yap; ilgili uyarı kutuları kendiliğinden kaybolur.
6. Yaş doğrulaması şu an yalnızca doğum yılına bakar; tam doğum tarihine geçilmeli.

## Tasarım
Arka plan `#0D0B12`, kart `#191522`, ana vurgu `#9B5CFF`, ikincil vurgu `#C7A7FF`.
Mor zemin üstündeki metin koyu (`#0D0B12`) seçildi; beyaz metin bu mordaki AA kontrastını sağlamıyor.
