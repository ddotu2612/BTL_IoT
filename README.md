# SpringIOT
- Báo cáo hệ thống giám sát lượng nước trong thành phố: https://drive.google.com/drive/folders/1qdfoCLDsXHXp1einD5LYVL3YaGFDA_Km?usp=sharing
- FrontEnd - BackEnd
- https://github.com/chuonghoang99/vue-project
- Các thư viện liên quan:
    - SpringMVC 4, Spring security
    - jdk-8, jdk-14
    - tomcat 8.5
    - MySQL 8.0.22
    - eclipseIDE
    - maven 3.6.3

- Khi clone project về chạy terminal>mvn clean install và update lại maven
- Luồng chạy: 
    + user đăng ký tài khoản username,password ->user đăng nhập -> trả về jwt(username) và user(authentication trong spring security)
    + user thêm mới device(đồng thời insert các sensor liên quan đến device) =>Trả về jwt(deviceID và jwt(username)) dùng để active device
    + Người dùng nạp code copy jwt(deviceID và user_token(username)) vào device thật và publish authentication server sẽ xác nhận và kích hoạt device(alive=1) sau đó trả về json(deviceID và device_token(userID,deviceID))
    + Thiết bị authen thành công và nhận được json(deviceID và device_token(userID,deviceID)) sẽ sử dụng nó để publish dữ liệu gửi lên server.
    + Cùng với việc esp32 gửi data thì nó cũng gửi keepAlive để báo cho server biết là mình đang còn sống ( deviceId,device_token,active=true).
    + Server sẽ dựa vào alive=1 để có insert vào db hay không.
    
- Các package trong project:
Mô hình MVC trong Spring và luồng dữ liệu chạy: View -> controller -> dto(->entity) Service -> dao ->database
    + Entity: Thực thể để map với database
    + Dao(Data Access Object): truy xuất tơi database thêm - cập nhật - sửa - xóa
    + Dto(Data Transfer Object):truyền dữ liệu qua lại giữa các tầng trong ứng dụng. Cụ thể là từ service -> dao 
    + Controller - API: Nơi nhận request
    + Service: tầng này sẽ xử lý nghiệp vụ nâng cao
    + Config: Nơi cấu hình các annotation trong spring: @Bean sản xuất ra đối tượng bean và được quản lý bởi Spring Container.Cấu hình mqtt, jpa, spring security,..
    + 

- Để khắc phục lỗi:"Could not write JSON: could not initialize proxy - no Session; nested exception is com.fasterxml.jackson.databind.JsonMappingException: could not initialize proxy - no Session"   Xẩy ra khi ta để fetch.LAZY ở prop trong class. Điều này xẩy ra là do JSON cố load LAZY nhưng nó đã kết thúc "trans". Do đó có vài cách khắc phục như thêm từ khóa @JsonIgnore ở prop có LAZY hoặc ta sẽ sử dụng "JOIN FETCH" để lấy cả dữ liệu của nó lên. Nhưng có cách đơn giản hơn là ta sẽ sử dụng 1 lớp nữa là DTO. trong lớp này ta sẽ bắt try{ những cái mà set, get LAZY) =>nó sẽ xẩy ra lỗi "could not initialize proxy - no Session" nhưng do bắt try rồi nên nó vẫn vượt qua được và lúc JSON gọi thì trả ở DTO (thay vì entity)

- Để lấy dữ liệu từ 3 bảng: Device-Sensor-DataSensor. Nếu ta để là Select t from DeviceEntity t sensorList s JOIN FETCH s.sensorDataList.
    + Nó sẽ xấy ra TH đặc biệt là Datasensor ko có gì thì nó sẽ ko query được vì dùng inner join
    + Khắc phục để là  Select t from DeviceEntity t sensorList s LEFT JOIN FETCH s.sensorDataList.



- ESP32:
    + Khi sử dụng thư viện PubSubClient thì kích thước tối đa gói tin gửi hoặc nhận sẽ là 128 bytes hoặc (256 byte) xem ở file PubSubClient.h và biến là MQTT_MAX_PACKET_SIZE. Để thay đổi giá trị vào file PubSubClient.h để thay đổi vidu 1024 
    + Cảm biến DHT22 hoặc DHT11 thì sử dụng thư viện SimpleDHT thay cho DHT(sử dụng cho arduino).
    
