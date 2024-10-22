//package vn.tayjava.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//
//import java.net.URI;
//
//@Configuration
//public class DynamoDbConfig {
//
//    @Bean
//    public DynamoDbClient dynamoDbClient() {
//        return DynamoDbClient.builder()
//                .endpointOverride(URI.create("http://localhost:8000"))
//                .region(Region.AP_SOUTHEAST_1) // ap-southeast-1: Default region name
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        // awsAccessKey2024: AWS Access Key ID
//                        // awsSecret2024: AWS Secret Access Key
//                        AwsBasicCredentials.create("awsAccessKey2024", "awsSecret2024")))
//                .build();
//    }
//}
