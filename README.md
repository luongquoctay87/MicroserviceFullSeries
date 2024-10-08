# Product Service
## I. Tổng quan về Elasticsearch
![Elasticsearch Component.png](gallery/Elasticsearch%20Component.png)

Elasticsearch là một công cụ tìm kiếm và phân tích phân tán mã nguồn mở dựa trên thư viện Apache Lucene. Nó được phát triển để cung cấp khả năng tìm kiếm nhanh chóng, mở rộng và phân tích dữ liệu phi cấu trúc.

Elasticsearch hiện nay là một trong những công cụ mạnh mẽ và phổ biến nhất cho việc tìm kiếm và phân tích dữ liệu phi cấu trúc, được sử dụng trong nhiều ngành công nghiệp và lĩnh vực khác nhau.

### 1. Cấu trúc dữ liệu cơ bản
- Document: Đơn vị dữ liệu cơ bản trong Elasticsearch, là một đối tượng JSON đại diện cho một bản ghi.
- Index: Một tập hợp các document có cùng loại, tương tự như một cơ sở dữ liệu trong RDBMS (Relational Database Management System).
- Shard: Elasticsearch chia index thành nhiều shard để lưu trữ và truy vấn song song. Mỗi shard là một phần của dữ liệu và có thể nằm trên các nút khác nhau trong một cụm (cluster).
- Replica: Là bản sao của các shard chính (primary shards), giúp tăng độ tin cậy và khả năng chịu lỗi.

### 2. Tính năng chính
- Tìm kiếm và phân tích: Elasticsearch có khả năng tìm kiếm toàn văn bản (full-text search) mạnh mẽ với tốc độ cao, có thể hỗ trợ truy vấn phức tạp như lọc, sắp xếp và phân tích dữ liệu.
- Phân tán (Distributed): Elasticsearch có thể dễ dàng mở rộng quy mô theo chiều ngang, phân tán dữ liệu và xử lý song song qua nhiều máy chủ khác nhau trong cụm.
- Thời gian thực gần (Near real-time): Cập nhật dữ liệu nhanh chóng và phản hồi các thay đổi ngay lập tức với độ trễ nhỏ.
- RESTful API: Elasticsearch tương tác chủ yếu qua giao diện RESTful API cho phép các ứng dụng có thể truy vấn và thao tác dữ liệu dễ dàng bằng cách gửi yêu cầu HTTP.

### 3. Các trường hợp sử dụng (Use cases)
- Tìm kiếm trong ứng dụng: Elasticsearch có thể được sử dụng để xây dựng các chức năng tìm kiếm trong website hoặc ứng dụng với hiệu suất cao.
- Log và phân tích dữ liệu: Phối hợp với các công cụ như Logstash và Kibana (trong Elastic Stack), Elasticsearch được sử dụng rộng rãi trong việc thu thập và phân tích log từ các hệ thống khác nhau.
- Phân tích dữ liệu thời gian thực: Khả năng xử lý dữ liệu thời gian thực của Elasticsearch giúp nó phù hợp trong việc giám sát hệ thống, phân tích hành vi người dùng hoặc xử lý dữ liệu IoT.

### 4. Cách hoạt động
- Indexing: Khi dữ liệu được thêm vào Elasticsearch nó sẽ được lập chỉ mục (index) giúp cho các truy vấn tìm kiếm nhanh chóng và hiệu quả.
- Search: Elasticsearch sử dụng các chỉ số đảo ngược (inverted index) để tìm kiếm nhanh chóng trong văn bản. Inverted index giúp xác định vị trí các từ khóa trong các document một cách nhanh chóng.
- Aggregations: Ngoài việc tìm kiếm Elasticsearch còn hỗ trợ các phép tính toán, phân nhóm và phân tích trên dữ liệu lớn, giúp người dùng hiểu rõ hơn về dữ liệu của mình.

### 5. Elastic Stack (ELK Stack)
Elasticsearch là trung tâm của bộ Elastic Stack (ELK), gồm:
- Logstash: Dùng để thu thập, xử lý và truyền tải dữ liệu đến Elasticsearch.
- Kibana: Giao diện trực quan để hiển thị và phân tích dữ liệu trong Elasticsearch.
- Beats: Các agent nhẹ được sử dụng để gửi dữ liệu từ các máy chủ, hệ thống đến Elasticsearch.

### 6. Cài đặt Elasticsearch
[Installing Elasticsearch](https://www.elastic.co/guide/en/elasticsearch/reference/current/install-elasticsearch.html)

## II. Tích hợp Elasticsearch, Postgres vào Product service
- PostgreSQL sẽ quản lý dữ liệu có cấu trúc và cung cấp khả năng truy vấn cơ bản thông qua JPA.
- Elasticsearch sẽ giúp bạn cung cấp khả năng tìm kiếm toàn văn (full-text search) và phân tích dữ liệu một cách nhanh chóng.

### 1. Dựng database Elasticsearch với docker
```yml
  elastic-search:
    image: elasticsearch:7.14.1
    container_name: elasticsearch
    restart: always
    ports:
      - "9200:9200"
    environment:
      - discovery.type=single-node
```
**Note:** để thiết lập authentication(username/password) cho elasticsearch: https://www.elastic.co/guide/en/elasticsearch/reference/7.14/security-minimal-setup.html

### 2. Cấu hình Elasticsearch trong Spring Boot

**a. Thêm dependency cho Elasticsearch**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
</dependency>
```

**b. Cấu hình kết nối trong application.properties hoặc application.yml**
```properties
spring.elasticsearch.uris=http://localhost:9200
#spring.elasticsearch.username=elastic
#spring.elasticsearch.password=changeme
```
**Note:** Vì không thiết lập authentication cho nên không cần username/password

**c. Tạo document và repository cho Elasticsearch**
- ProductDocument
```java
@Getter
@Setter
@Document(indexName = "products")
public class ProductDocument {
    @Id
    private String id;

    private String name;
    private String description;
    private double price;
}
```

- ProductSearchRepository
```java
@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, String> {
    List<ProductDocument> findByNameContaining(String name);
}
```

### 2. Cấu hình Postgres trong Spring Boot
**a. Thêm dependency cho PostgreSQL**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

**b. Cấu hình kết nối trong application.properties hoặc application.yml**
```yml
spring:
  datasource:
    url: ${POSTGRES_URL:jdbc:postgresql://localhost:5432/postgres}
    username: ${POSTGRES_USER:quoctay}
    password: ${POSTGRES_PASSWORD:password}
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: false
    hibernate:
      ddl-auto: none
```

**c. Sử dụng JPA cho PostgreSQL**
- Product
```java
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @CreationTimestamp
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
```

- ProductRepository
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
}
```

### 3. Đồng bộ dữ liệu giữa PostgreSQL và Elasticsearch
Chúng ta có thể thực hiện đồng bộ hóa dữ liệu giữa PostgreSQL và Elasticsearch bằng cách sử dụng một service để lưu dữ liệu vào cả hai hệ thống.

```java
@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT-SERVICE")
public class ProductServiceImpl implements ProductService {

    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addProduct(ProductCreationRequest request) {
        log.info("Add product {}", request);

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());

        productRepository.save(product);

        if (product.getId() != null) {
            ProductDocument productDocument = new ProductDocument();
            productDocument.setName(request.getName());
            productDocument.setDescription(request.getDescription());
            productDocument.setPrice(request.getPrice());

            productSearchRepository.save(productDocument);
            log.info("Save productDocument", productDocument);
        }

        return product.getId();

    }

    @Override
    public List<ProductDocument> searchProducts(String name) {
        log.info("Search products by name {}", name);

        List<ProductDocument> productDocuments = new ArrayList<>();

        if (StringUtils.hasLength(name)) {
            productDocuments = productSearchRepository.findByNameContaining(name);
        } else {
            Iterable<ProductDocument> documents = productSearchRepository.findAll();
            for (ProductDocument productDocument : documents) {
                productDocuments.add(productDocument);
            }
        }

        return productDocuments;
    }
}
```

## 4. Tích hợp Product Service vào api-gateway
```yml
spring:
  config:
    activate:
      on-profile: dev
  devtools:
    add-properties: true
  cloud:
    gateway:
      routes: # điều hướng request đến service tương ứng thông qua chỉ định trên url
        - id: product-service
          uri: http://localhost:8083
          predicates:
            - Path=/product/**, /v3/api-docs/product-service
          filters:
            - RewritePath=/product/(?<segment>.*), /product/$\{segment} # thay thế ký tự /product/ thành /
```

## 5. Test API
- Add product
```bash
curl --location 'http://localhost:8083/product/add' \
--header 'accept: */*' \
--header 'Content-Type: application/json' \
--data '{
  "name": "Hảo hảo",
  "description": "Mì tôm quốc dân",
  "price": 0
}'
```

- get product
```bash
curl --location 'http://localhost:4953/product/list' \
--header 'accept: */*'
```