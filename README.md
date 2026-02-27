# Voucher Generator (Android Kotlin)

A Jetpack Compose Android app that accepts voucher fields and generates a PNG voucher image.

## Features
- Input fields: `Name`, `Address`, `Date` (default today), dynamic item rows.
- Item row fields: `Item`, `Price`, `Quantity`.
- `+ Add Item` / `Remove` item.
- Live calculation: item subtotal, overall subtotal, all total.
- Generate PNG voucher to Gallery (`Pictures/Vouchers`).
- Myanmar Unicode support with bundled `NotoSansMyanmar-Regular.ttf`.

## Project Path
`/Users/myosetpaing/Documents/dtis-central-backend/voucher-generator-android`

## Open and Run
1. Open `voucher-generator-android` in Android Studio.
2. Let Gradle sync and download dependencies.
3. Run on emulator or device (Android 7.0+).
4. Tap `Generate PNG Voucher` and check Gallery.

## Run with VS Code (No Android Studio)
1. Open this folder in VS Code.
2. Connect an Android phone with USB debugging enabled.
3. In VS Code: `Terminal -> Run Task... -> Android: Run on Connected Device`.

If you prefer terminal commands:
```bash
cd /Users/myosetpaing/Documents/dtis-central-backend/voucher-generator-android
./scripts/android-env.sh adb devices
./scripts/android-env.sh ./gradlew installDebug
./scripts/android-env.sh adb shell am start -n com.example.vouchergenerator/.MainActivity
```

Optional emulator-only setup (CLI):
```bash
yes | ./scripts/android-env.sh sdkmanager --licenses
./scripts/android-env.sh sdkmanager --install "cmdline-tools;latest" "emulator" "system-images;android-34;google_apis;arm64-v8a"
echo "no" | ./scripts/android-env.sh avdmanager create avd -n Pixel_6_API_34 -k "system-images;android-34;google_apis;arm64-v8a"
./scripts/android-env.sh emulator -avd Pixel_6_API_34
```

## Notes
- Date input is plain text in `YYYY-MM-DD` format.
- Current implementation uses `Subtotal == All Total` (no tax/discount).
