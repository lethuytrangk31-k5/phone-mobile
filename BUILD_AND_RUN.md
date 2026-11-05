# Hướng Dẫn Build & Chạy Dự Án Phone Mobile

Tài liệu này cung cấp hướng dẫn chi tiết từng bước để thiết lập môi trường, biên dịch (build), chạy ứng dụng và kiểm thử mã nguồn trên các nền tảng khác nhau.

---

## 1. Yêu cầu môi trường (Prerequisites)

Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã cài đặt các công cụ sau:

- **Hệ điều hành:** Linux (Ubuntu 20.04+, Fedora...), macOS hoặc Windows 10/11.
- **Java Development Kit (JDK):** JDK 17 hoặc mới hơn (JDK 21, OpenJDK 25 hoặc JetBrains Runtime đi kèm Android Studio).
- **Android Studio:** Phiên bản Android Studio Ladybug / Koala / Jellyfish trở lên (đã bao gồm Android SDK).
- **Android SDK:**
  - `compileSdk`: 37
  - `minSdk`: 24 (Android 7.0 Nougat trở lên)
  - `targetSdk`: 37

---

## 2. Cách 1: Sử dụng Android Studio (Khuyến nghị)

Đây là cách đơn giản và trực quan nhất để phát triển và trải nghiệm ứng dụng:

### Bước 1: Mở dự án
1. Khởi động **Android Studio**.
2. Chọn **File** > **Open...** (hoặc chọn *Open* từ màn hình chào mừng).
3. Điều hướng và chọn thư mục dự án:  
   `/home/snow2003/HongNgoc/phone-mobile`
4. Bấm **OK**.

### Bước 2: Đồng bộ Gradle (Sync Project)
- Android Studio sẽ tự động tải các thư viện cần thiết và đồng bộ dự án (Gradle Sync).
- Nếu không thấy tự chạy, bấm vào biểu tượng **Sync Project with Gradle Files** (biểu tượng con voi có mũi tên xoay tròn ở góc trên bên phải).
- Chờ cho thanh tiến trình thông báo `BUILD SUCCESSFUL`.

### Bước 3: Chọn thiết bị và Chạy ứng dụng
1. Trên thanh công cụ trên cùng, tại mục **Device Manager**, chọn thiết bị muốn chạy:
   - **Máy ảo (Android Emulator):** Tạo máy ảo qua *Device Manager* (Pixel 7/8 với Android 13/14).
   - **Thiết bị thật:** Cắm điện thoại Android vào máy tính qua cáp USB và bật chế độ **USB Debugging** (Gỡ lỗi qua USB) trong cài đặt Nhà phát triển.
2. Bấm nút **Run** (biểu tượng tam giác xanh ▶️) hoặc nhấn tổ hợp phím **`Shift + F10`** (trên Windows/Linux) / **`Control + R`** (trên macOS).
3. Ứng dụng sẽ tự động được biên dịch, cài đặt và mở lên trên màn hình thiết bị.

---

## 3. Cách 2: Biên dịch và chạy bằng Dòng lệnh (CLI)

Nếu bạn muốn làm việc trực tiếp trên Terminal / Command Line mà không cần mở giao diện Android Studio:

### 3.1. Thiết lập biến môi trường JAVA_HOME (nếu chưa có)

- **Trên Linux (với Android Studio Snap):**
  ```bash
  export JAVA_HOME=/snap/android-studio/current/jbr
  export PATH=$JAVA_HOME/bin:$PATH
  ```

- **Trên Linux (với JDK cài đặt qua apt):**
  ```bash
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
  export PATH=$JAVA_HOME/bin:$PATH
  ```

- **Trên macOS:**
  ```bash
  export JAVA_HOME=$(/usr/libexec/java_home -v 17)
  ```

- **Trên Windows (PowerShell):**
  ```powershell
  $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
  ```

### 3.2. Cấp quyền thực thi cho Gradle Wrapper (Linux / macOS)
```bash
chmod +x ./gradlew
```

### 3.3. Lệnh biên dịch mã nguồn (Compile Kotlin)
Kiểm tra cú pháp và biên dịch code Kotlin mà không cần đóng gói APK:
```bash
./gradlew compileDebugKotlin
```

### 3.4. Lệnh đóng gói file APK (Assemble Debug)
Biên dịch toàn bộ tài nguyên, XML và đóng gói thành file APK hoàn chỉnh:
- **Linux / macOS:**
  ```bash
  ./gradlew assembleDebug
  ```
- **Windows:**
  ```cmd
  gradlew.bat assembleDebug
  ```

Sau khi build thành công (`BUILD SUCCESSFUL`), file APK được tạo ra tại đường dẫn:
```
app/build/outputs/apk/debug/app-debug.apk
```

### 3.5. Cài đặt và khởi chạy APK qua ADB (Android Debug Bridge)
Nếu thiết bị hoặc máy ảo đã được kết nối:

1. **Cài đặt file APK vào máy:**
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

2. **Khởi chạy ứng dụng trực tiếp:**
   ```bash
   adb shell am start -n com.example.phone_mobile/.MainActivity
   ```

---

## 4. Chạy Kiểm Thử Đơn Vị (Unit Tests)

Dự án bao gồm các bộ unit test kiểm tra tính chính xác của:
- Thuật toán bóc tách dấu tiếng Việt phục vụ tìm kiếm không dấu (`removeVietnameseDiacritics`).
- Thuật toán tìm kiếm danh bạ không dấu (`nguyen` -> `Nguyễn Văn A`).
- Thuật toán xác thực và phát hiện số điện thoại trùng lặp trong danh bạ.

Chạy toàn bộ unit test bằng lệnh:
```bash
./gradlew testDebugUnitTest
```

Kết quả báo cáo chi tiết dạng HTML sẽ được sinh ra tại:
```
app/build/reports/tests/testDebugUnitTest/index.html
```

---

## 5. Xử lý sự cố thường gặp (Troubleshooting)

### Lỗi 1: `ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH`
- **Nguyên nhân:** Máy tính chưa cài Java hoặc chưa cấu hình biến môi trường `JAVA_HOME`.
- **Cách khắc phục:** Cài đặt JDK 17+ hoặc trỏ `JAVA_HOME` đến thư mục `jbr` có sẵn trong bộ cài Android Studio như hướng dẫn ở mục 3.1.

### Lỗi 2: `Permission denied: ./gradlew`
- **Nguyên nhân:** File `gradlew` chưa được cấp quyền thực thi trên Linux/macOS.
- **Cách khắc phục:** Chạy lệnh `chmod +x ./gradlew`.

### Lỗi 3: `SDK location not found`
- **Nguyên nhân:** Chưa định nghĩa đường dẫn Android SDK trong file cấu hình local.
- **Cách khắc phục:** Tạo hoặc kiểm tra file `local.properties` ở thư mục gốc của dự án với nội dung:
  - Trên Linux: `sdk.dir=/home/<username>/Android/Sdk`
  - Trên macOS: `sdk.dir=/Users/<username>/Library/Android/sdk`
  - Trên Windows: `sdk.dir=C:\\Users\\<username>\\AppData\\Local\\Android\\Sdk`
