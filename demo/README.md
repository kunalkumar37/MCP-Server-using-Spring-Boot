# MCPServer - EV Charger Service

A Spring Boot-based Model Context Protocol (MCP) Server for managing EV charging records. This server provides tools for tracking and managing electric vehicle charging operations.

## Overview

MCPServer is an MCP-compliant server built with Spring Boot that exposes a suite of tools for managing EV charger operations. It uses the Spring AI framework for MCP server implementation and provides stdio-based communication for MCP protocol integration.

## Features

- **Tool-based API**: 6 registered tools for EV charger management
- **MCP Protocol Support**: Full Model Context Protocol (MCP) compliance
- **Synchronous Operation**: Efficient SYNC mode for tool execution
- **In-Memory Storage**: Charging records stored in concurrent hash maps
- **Graceful Shutdown**: Proper resource cleanup on termination

## Architecture

`
McpServerApplication
 EVChargerService (Tool Provider)
    addChargingRecord
    getAllChargingRecords
    getRecordByCharger
    getRecordByVehicle
    getRecordByStatus
    deleteChargingRecord
 Spring AI MCP Server Auto-configuration
`

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Spring Boot 3.5.10
- Spring AI 1.1.2

## Setup

1. Clone or navigate to the project directory:
`ash
cd D:\MCPServer\demo
`

2. Build the project:
`ash
mvn clean install
`

## Running the Application

Start the MCP Server with the following command:

`ash
cd D:\MCPServer\demo
mvn org.springframework.boot:spring-boot-maven-plugin:run
`

The server will:
- Start on **port 8080**
- Register 6 EV Charger Service tools
- Enable MCP protocol communication
- Log output to ./logs/mcp-MCPServer.log

Expected startup output:
`
Starting McpServerApplication using Java 21.0.9
Registered tools: 6
Tomcat started on port 8080 (http) with context path '/'
Started McpServerApplication in 5.403 seconds
`

## Available Tools

### 1. addChargingRecord
**Description**: Add a new charging record to the EV CHARGER system.

**Parameters**:
- 	imestamp (String): Date and time (format: M/dd/yyyy HH:mm:ss)
- chargerId (String): Unique charger identifier
- ehicleId (String): Vehicle identifier
- powerDrawKw (double): Power consumption in kilowatts
- 	ransformerLoadKw (double): Transformer load in kilowatts
- chargingStatus (String): Current charging status

### 2. getAllChargingRecords
**Description**: Retrieve all charging records from the system.

**Returns**: List of ChargingRecord objects with complete details

### 3. getRecordByCharger
**Description**: Get all charging records for a specific charger ID.

**Parameters**:
- chargerId (String): Charger identifier to filter by

**Returns**: List of records for the specified charger

### 4. getRecordByVehicle
**Description**: Get all charging records for a specific vehicle ID.

**Parameters**:
- ehicleId (String): Vehicle identifier to filter by

**Returns**: List of records for the specified vehicle

### 5. getRecordByStatus
**Description**: Get all charging records by charging status.

**Parameters**:
- chargingStatus (String): Status to filter by (e.g., "Charging", "Completed", "Paused")

**Returns**: List of records with the specified status

### 6. deleteChargingRecord
**Description**: Delete a charging record from the system.

**Parameters**:
- chargerId (String): Charger identifier
- ehicleId (String): Vehicle identifier
- 	imestamp (String): Record timestamp

**Returns**: Success or error message

## Configuration

Configuration is managed via src/main/resources/application.properties:

`properties
spring.application.name=MCPServer

# MCP Server Configuration
spring.ai.mcp.server.type=SYNC
spring.ai.mcp.server.name=MCPServer
spring.ai.mcp.server.version=1.0.0

# Disable web application type (required for MCP stdio communication)
spring.main.web-application-type=none
spring.main.banner-mode=off

# Logging configuration
logging.file.name=./logs/mcp-MCPServer.log
`

## Project Structure

`
D:\MCPServer\demo
 src/
    main/
       java/com/mcp/demo/mcp_EV_Charger_Service/
          McpServerApplication.java
          EVChargerService.java
       resources/
           application.properties
    test/
 target/
 pom.xml
 README.md
`

## Dependencies

- **spring-boot-starter-parent**: 3.5.10-SNAPSHOT
- **spring-ai-starter-mcp-server**: 1.1.2
- **spring-boot-starter-web**: For servlet environment support
- **spring-boot-starter-test**: For testing (scope: test)

## Building

`ash
# Clean build
mvn clean install

# Build without running tests
mvn clean install -DskipTests

# Compile only
mvn compile
`

## Troubleshooting

### Port 8080 already in use
Change the port in pplication.properties:
`properties
server.port=8081
`

### Missing dependencies
Ensure you have internet connectivity for Maven to download dependencies:
`ash
mvn dependency:resolve
`

### Application won't start
Check the logs:
`ash
tail -f logs/mcp-MCPServer.log
`

## Sample Data

The application initializes with sample charging records:
- Charger: EVCH001
- Vehicle: EV123
- Timestamps: 9/4/2025 8:00, 8:10, 8:20 (sample times)

## Development

### Running in IDE
1. Open the project in IntelliJ IDEA or VS Code
2. Run McpServerApplication.main() method directly
3. Or use Maven Run Configuration with goal: org.springframework.boot:spring-boot-maven-plugin:run

### Adding New Tools
1. Add new @Tool annotated methods to EVChargerService.java
2. Rebuild and restart the application
3. Tools are auto-discovered and registered

## Stopping the Application

Press Ctrl+C in the terminal where the application is running, or use:

`ash
# Kill by process ID (if running in background)
taskkill /PID <PID> /F
`

## License

This project is part of the MCP Server ecosystem.

## Support

For issues or questions, refer to:
- Spring AI Documentation: https://spring.io/projects/spring-ai
- MCP Specification: https://modelcontextprotocol.io/
