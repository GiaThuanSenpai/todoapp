To-Do List API

📌 Giới thiệu

Đây là một RESTful API để quản lý danh sách công việc (to-do list). API hỗ trợ các chức năng như tạo, đọc, cập nhật, xóa công việc, thiết lập phụ thuộc giữa các công việc, kiểm tra vòng lặp phụ thuộc, và tối ưu hóa hiệu suất.

- Cách cài đặt
  + Download Spring Tool Suite hoặc Intellij ide (Ở đây dùng STS): https://spring.io/tools
  + Tải maven, jdk-21, tomcat 9.0
    . https://maven.apache.org/download.cgi: Download maven
    . https://tomcat.apache.org/download-90.cgi: Download tomcat
    . https://www.oracle.com/java/technologies/downloads/#java21: Download jdk-21
  + Cài đặt biến môi trường cho maven và jdk-21
  + Mở chương trình trong Spring Tool Suite
    . Cấu hình maven: Window -> Preferences -> Maven -> User Settings -> Global Settings -> Tìm đến file settings.xml trong conf ở nơi chứa maven đã tải về
    . Cấu hình tomcat:  Window -> Preferences -> Server -> Runtime Environments -> Add -> Apache -> Chon Tomcate 9.0
    . Cấu hình JDK:  Window -> Preferences ->  Java -> Installed JREs -> Add -> Tìm đến thư mục chứa JDK
  + Tìm application.properties: Chỉnh sửa connect CSDL
