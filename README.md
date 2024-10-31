# Hyperledger Fabric Spring REST API

A Spring Boot REST API integration with Hyperledger Fabric test-network, providing endpoints to interact with the basic asset transfer chaincode.

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker and Docker Compose
- Hyperledger Fabric 2.5+
- Git

## 🏗️ Technology Stack

- Spring Boot 3.x
- Hyperledger Fabric Java SDK
- Swagger UI (OpenAPI 3.0)
- Maven
- Docker

## 🔧 Required Fabric Components

This project utilizes the following components from the `fabric-samples` repository:
- `bin/` - Fabric binaries and tools
- `config/` - Core configuration files
- `test-network/` - Basic Fabric network setup
- `asset-transfer-basic/` - Chaincode implementation

## 🚀 Getting Started

### 1. Clone and Setup

```bash
# Clone the repository
git clone https://github.com/yourusername/fabric-spring-api.git
cd fabric-spring-api

# Start the Fabric test network
cd fabric-samples/test-network
./network.sh down
./network.sh up createChannel -c mychannel 
./network.sh deployCC -ccn basic -ccp ../asset-transfer-basic/chaincode-java -ccl go
```

### 2. Configure Application

Ensure your `application.properties` or `application.yaml` contains:

```yaml
fabric:
  chaincode-name: basic
  channel-name: mychannel
  msp-id: Org1MSP
  peer-endpoint: localhost:7051
  override-auth: peer0.org1.example.com
```

## 📚 API Documentation

Access the Swagger UI documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

## 🔄 Available Endpoints

| Method | Endpoint | Description | Function |
|--------|----------|-------------|-----------|
| POST | `/assets/api/v1/int` | Initialize the ledger | `InitLedger()` |
| GET | `/assets/api/v1/all` | Retrieve all assets | `GetAllAssets()` |
| POST | `/assets/api/v1` | Create a new asset | `CreateAsset()` |
| GET | `/assets/api/v1/{assetId}` | Get asset by ID | `ReadAsset()` |
| PUT | `/assets/api/v1/{assetId}` | Update asset | `UpdateAsset()` |
| DELETE | `/assets/api/v1/{assetId}` | Delete asset | `DeleteAsset()` |

## 📝 Usage Examples

### Initialize Ledger
```bash
http POST http://localhost:8080/assets/api/v1/int

```

### Get All Assets
```bash
http GET http://localhost:8080/assets/api/v1/all
```

### Create Asset
```bash
http POST http://localhost:8080/assets/api/v1 \
Content-Type:application/json \
id="asset1" color="blue" size:=5 owner="Tom" appraisedValue:=100

```

### Read Asset
```bash
http GET http://localhost:8080/assets/api/v1/asset1

```

### Update Asset
```bash
http PUT http://localhost:8080/assets/api/v1/asset1 \
Content-Type:application/json \
color="red" size:=10 owner="Jerry" appraisedValue:=200

```

### Delete Asset
```bash
http DELETE http://localhost:8080/assets/api/v1/asset1

```

## ⚠️ Error Handling

The API includes comprehensive error handling for common scenarios:
- Network connectivity issues
- Invalid asset IDs
- Duplicate assets
- Missing required fields
- Unauthorized access

## 🔒 Security

This implementation uses the default security settings from the Fabric test network. For production deployments, ensure proper security measures are implemented:
- TLS certificates
- Proper MSP configuration
- Access control policies
- Environment-specific configurations

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📮 Support

For issues and questions, please open an issue in the GitHub repository.