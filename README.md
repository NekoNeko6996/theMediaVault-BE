# The Media Vault

## Tổng Quan Dự Án
The Media Vault là một hệ thống REST API được xây dựng bằng Spring Boot, cung cấp các giải pháp quản lý thư mục và tập tin toàn diện. Hệ thống sử dụng MinIO làm máy chủ lưu trữ đối tượng (Object Storage), MySQL để quản lý dữ liệu quan hệ và JSON Web Token (JWT) để bảo mật xác thực.

## Công Nghệ Sử Dụng
- Java
- Spring Boot (Web, Security, Data JPA)
- MySQL
- MinIO
- JWT (JSON Web Token)
- MapStruct & Lombok

## Giải Thích Cấu Trúc & Các API Use Case
Dưới đây là tài liệu giải thích chi tiết về các luồng xử lý (Use Cases) của hệ thống, kèm theo sơ đồ tuần tự (Sequence Diagram) minh họa cho từng API.

### 1. Xác Thực & Người Dùng (Authentication)
Các API liên quan đến việc định danh và cấp quyền truy cập cho người dùng.

- **Đăng ký (Register)**: Xử lý quá trình tạo tài khoản mới. Thông tin người dùng được mã hóa mật khẩu an toàn trước khi lưu vào cơ sở dữ liệu.
  ![Register Sequence](idea/diagrams/Sequence%20Diagram/register_sequence.png)

- **Đăng nhập (Login)**: Xác thực thông tin đăng nhập của người dùng. Nếu thành công, hệ thống sẽ sinh ra một JWT (JSON Web Token) để client sử dụng trong các request tiếp theo.
  ![Login Sequence](idea/diagrams/Sequence%20Diagram/login.png)

### 2. Quản Lý Tập Tin & Thư Mục (File & Folder Management)
Các API cốt lõi dùng để thao tác với dữ liệu trên hệ thống.

- **Tải lên tập tin (Upload Files)**: Hệ thống kiểm tra tính hợp lệ và quyền tải lên của người dùng. Sau đó, tập tin vật lý được đẩy lên máy chủ MinIO và siêu dữ liệu (metadata) được ghi lại vào MySQL.
  - ![Check Files Can Upload](idea/diagrams/Sequence%20Diagram/CheckFilesCanUpload_sequence.png)
  - ![Upload Files](idea/diagrams/Sequence%20Diagram/UploadFiles_sequence.png)

- **Lấy danh sách (Get Files)**: Truy xuất danh sách các tập tin và thư mục con nằm bên trong một thư mục cụ thể.
  ![Get Files](idea/diagrams/Sequence%20Diagram/GetFiles_sequence.png)

- **Lấy thông tin chi tiết (Get File Info)**: Xem các metadata chi tiết của một tập tin cụ thể như kích thước, định dạng, đường dẫn, thời gian tạo.
  ![Get File Info](idea/diagrams/Sequence%20Diagram/GetFileInfo_sequence.png)

- **Tạo thư mục mới (Create Folder)**: Tạo một cấu trúc thư mục logic mới để sắp xếp file.
  ![Create Folder](idea/diagrams/Sequence%20Diagram/CreateFolder_sequence.png)

- **Đổi tên tập tin/thư mục (Rename File)**: Cập nhật lại tên của đối tượng đang lưu trữ.
  ![Rename File](idea/diagrams/Sequence%20Diagram/RenameFile_sequence.png)

- **Di chuyển tập tin (Move Files)**: Đổi vị trí của tập tin hoặc thư mục sang một thư mục đích khác trên hệ thống.
  ![Move Files](idea/diagrams/Sequence%20Diagram/MoveFiles_sequence.png)

- **Nhân bản tập tin (Duplicate Files)**: Tạo nhanh một bản sao của tập tin hiện tại.
  ![Duplicate Files](idea/diagrams/Sequence%20Diagram/DuplicateFiles_sequence.png)

- **Lấy URL Tải xuống (Get Download URL)**: API tương tác với MinIO để tạo và trả về một đường dẫn (Pre-signed URL) cho phép client tải file trực tiếp.
  ![Get Download URL](idea/diagrams/Sequence%20Diagram/GetDownloadUrl_sequence.png)

- **Tìm kiếm tập tin (Search Files)**: Cho phép tìm kiếm các tập tin và thư mục trong không gian lưu trữ thông qua từ khóa.
  ![Search Files](idea/diagrams/Sequence%20Diagram/SearchFilesByKeyword_sequence.png)

### 3. Đánh Dấu Yêu Thích (Starred Files)
Quản lý các tập tin quan trọng được người dùng quan tâm.

- **Đánh dấu/Bỏ đánh dấu (Toggle Starred File)**: Thêm hoặc xóa một tập tin khỏi danh sách yêu thích cá nhân.
  ![Toggle Starred](idea/diagrams/Sequence%20Diagram/ToggleStarredFile_sequence.png)

- **Lấy danh sách yêu thích (Get All Starred Files)**: Truy xuất danh sách tất cả các tập tin đã được đánh dấu sao.
  ![Get All Starred Files](idea/diagrams/Sequence%20Diagram/GetAllStarredFiles_sequence.png)

### 4. Thùng Rác & Xóa Dữ Liệu (Trash & Deletion)
Các luồng API xử lý việc xóa, dọn dẹp và phục hồi dữ liệu một cách an toàn.

- **Chuyển vào thùng rác (Move to Trash)**: Xóa mềm (Soft Delete) đối tượng. File/Folder không bị mất đi hoàn toàn mà được ẩn khỏi giao diện chính và chuyển vào không gian thùng rác.
  ![Move to Trash](idea/diagrams/Sequence%20Diagram/MoveAllToTrash_sequence.png)

- **Xem thùng rác (Get Trash Files)**: Lấy danh sách các tập tin đang nằm trong thùng rác chờ xử lý.
  ![Get Trash Files](idea/diagrams/Sequence%20Diagram/GetTrashFiles_sequence.png)

- **Khôi phục tập tin (Restore Files)**: Đưa tập tin từ thùng rác trở về trạng thái và vị trí thư mục gốc ban đầu của nó.
  ![Restore Files](idea/diagrams/Sequence%20Diagram/RestoreFilesFromTrash_sequence.png)

- **Dọn sạch thùng rác (Empty Trash)**: Xóa vĩnh viễn (Hard Delete) toàn bộ các tập tin đang có trong thùng rác ra khỏi MinIO và MySQL.
  ![Empty Trash](idea/diagrams/Sequence%20Diagram/EmptyTrash_sequence.png)

- **Xóa vĩnh viễn (Hard Delete)**: Bỏ qua thùng rác và tiến hành xóa vĩnh viễn ngay lập tức dữ liệu của người dùng.
  ![Hard Delete](idea/diagrams/Sequence%20Diagram/HardDeleteFiles_sequence.png)

## Hướng Dẫn Cài Đặt (Getting Started)

### 1. Yêu Cầu Môi Trường
- Java Development Kit (JDK)
- Apache Maven
- Máy chủ cơ sở dữ liệu MySQL
- Máy chủ lưu trữ MinIO

### 2. Cấu Hình Ứng Dụng
Đảm bảo bạn đã thay đổi các tham số kết nối đến Database và MinIO trong tệp `application.properties` hoặc `application.yml` tại `src/main/resources`.

### 3. Biên Dịch Và Khởi Chạy
Mở terminal tại thư mục gốc của dự án và chạy các lệnh sau:

Build dự án:
```bash
mvn clean install
```

Chạy dự án:
```bash
mvn spring-boot:run
```
