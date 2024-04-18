# Các bước thực hiện
## Sơ đồ tổng quan

![image](/simple-product-restfulAPI.jpg)

## Khởi tạo MySQL database
Cài đặt Mysql và Portainer với Docker trong WSL.

File docker-compose.yml như sau:

```yaml
version: '3.7'

services:
  mysql:
    image: mysql:latest
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: abc123-
      MYSQL_DATABASE: productdb
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  portainer:
    image: portainer/portainer-ce:latest
    restart: always
    ports:
      - "9000:9000"
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      - portainer_data:/data

volumes:
  mysql_data:
  portainer_data:

```
## Khởi tạo Maven project

Sử dụng các dependencies:
* Spring Web
* Spring Data JPA
* MySQL JDBC Driver
* Lombok

## Cấu hình datasource trong application.properties

Config này giúp kết nối với database.

```
spring.application.name=productapp
spring.datasource.url=jdbc:mysql://localhost:3306/productdb
spring.datasource.username=root
spring.datasource.password=abc123-
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.show_sql=true
```

## Code Entity Class

Entity sẽ là Product với các field:

```java
public class Product {

    private Integer id;
    private String name;
    private float price;

}
```

## Code Repository Interface

Tạo một interface Repository extends từ JpaRepository.

## Code UserRepostoryTests (unit test for data access layer)

Ta sẽ test các chức năng theo CRUD.

### Create

* Tạo mới một Product.
* Kiểm tra xem Product đó có tồn tại hay không.

### Read

#### Kiểm tra `findAll()`

* Duyệt qua tất cả `Product`.

#### Kiểm tra `findById()`

* Tạo ra một instance `Optional` dựa trên `id`.
* Kiểm tra xem `Optional` đó có tồn tại hay không.

### Update

* Lấy ra `product` dựa trên `id` truyền vào.
* Thay đổi giá trị của các field.
* Lưu lại.
* Kiểm tra xem giá trị vừa thay đổi có đúng không.

### Delete

* Xóa `product` dựa trên `id`.
* Kiểm tra xem `product` đó còn tồn tại không.

## Code ServiceImpl Class

Đầu tiên nên tạo một interface `ProductService` với các function:

```java
    // Create
    Product createProduct(Product product);

    // Read
    Iterable<Product> getAllProducts();
    Product getProductById(Integer id);

    // Update
    Product updateProduct(Integer id, Product product);

    // Delete
    void deleteProduct(Integer id);
```

Sau đó tạo `ProductSerivceImpl` rồi implement tất cả các method của `ProductService`.

Sở dĩ cần làm thêm một bước như vậy để giúp tăng tính mở rộng code.

Gỉa sử hiện tại mình đang có UserService và UserServiceImpl hoạt động ổn. Về sau này chúng ta cần chỉnh sửa một số tính năng trong UserServiceImpl để tăng performance hoặc adapt cho phù hợp với business mới. Nhưng do UserServiceImpl đã được phát triển qua một thời gian dài nên việc sửa code trong này rất khó khăn, lúc đó chúng ta có thể tạo ra một UserServiceImplNew mới cũng implement UserService và triển khai code tách bạch khỏi UserServiceImpl. Sau đó chúng ta chỉ cần thay đổi việc sử dụng UserServiceImplNew thay vì UserServiceImpl mà không cần chỉnh sửa code ở những chỗ khác.

Những chỗ khác đang sử dụng UserServiceImpl mà không có nhu cầu chỉnh sửa thì vẫn có thể sử dụng, do không có chỉnh sửa gì trên UserServiceImpl nên chúng ta yên tâm là các tính năng sẽ vẫn hoạt động như cũ.

## Code REST Controller Class: RESTful CRUD API

Khởi tạo lớp Controller với anotation `@RestController` với `@RequestMapping("/product/api")`

Tạo các method tương ứng với CRUD

```java
    // Create
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product newProduct = service.createProduct(product);
        return new ResponseEntity<Product>(newProduct, HttpStatus.CREATED);
    }

    // Read
    @GetMapping("/products")
    public Iterable<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = service.getProductById(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }
    
    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        Product updatedProduct = service.updateProduct(id, product);
        return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        service.deleteProduct(id);
    }
```

## Test using Postman

Sử dụng phần mềm Postman để test RESTful API, hoặc cũng có thể sử dụng curl.