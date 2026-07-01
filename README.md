# LangChain4j Spring Boot Demo

A Spring Boot application demonstrating LangChain4j integration with OpenAI's GPT-4 for AI-powered chat capabilities.

## Tech Stack

- **Framework**: Spring Boot 4.1.0
- **Language**: Java 26.0.1
- **Build Tool**: Maven 3
- **AI Framework**: LangChain4j 1.17.0
- **LLM Provider**: OpenAI (GPT-4o-mini)
- **Web Server**: Apache Tomcat 11.0.22
- **Dependency Management**: Maven Central

### Key Dependencies
- `langchain4j-core`: Core LangChain4j framework
- `langchain4j-open-ai`: OpenAI integration
- `spring-boot-starter-web`: Spring Web MVC
- `spring-boot-devtools`: Development tools with auto-reload

## Project Structure

```
langchain4jdemo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── ChatController.java          # REST API endpoints
│   │   │       ├── Langchain4jdemoApplication.java  # Main Spring Boot app
│   │   │       ├── config/
│   │   │       │   └── LangChain4jConfig.java   # LangChain4j configuration
│   │   │       └── model/
│   │   │           └── Assistant.java           # AI Assistant interface
│   │   └── resources/
│   │       └── application.properties           # Application configuration
│   └── test/
│       └── java/
│           └── com/example/demo/
│               └── Langchain4jdemoApplicationTests.java
├── pom.xml                                      # Maven configuration
└── .vscode/launch.json                         # VS Code debug configuration
```

## API Endpoints

### Chat Endpoint

**URL**: `http://localhost:8080/api/chat/{message}`

**Method**: `GET`

**Parameters**:
- `message` (path parameter): The user's message to send to the AI assistant

**Example Requests**:
```bash
# Request 1 - First request (allowed)
curl -X GET "http://localhost:8080/api/chat/hello"

# Response (success - if API key is valid)
{
  "response": "Hello! How can I help you today?"
}

# Request 2 - Second request within 60 seconds (rate limited)
curl -X GET "http://localhost:8080/api/chat/how are you"

# Response (rate limited)
Rate limit exceeded. Maximum 1 request per minute allowed. Try again in XXXX ms
```

**Response Types**:
- `200 OK`: Successful response from the AI assistant
- `429 Too Many Requests` (implicit): Rate limit exceeded (1 request per 60 seconds)
- `500 Internal Server Error`: API key validation failed or other errors

**Error Responses**:
```json
{
  "error": "OpenAI API authentication failed. Check if OPENAI_API_KEY is valid. Details: {error details}"
}
```

## Features

### 1. AI-Powered Chat
- Integration with OpenAI's GPT-4o-mini model
- Professional IT support assistant system prompt
- Conversational AI responses

### 2. Rate Limiting
- **Limit**: 1 request per 60 seconds (per application instance)
- **Implementation**: Thread-safe atomic counters
- **Auto-Reset**: Counter resets after time window expires

### 3. Error Handling
- Graceful API key validation
- Clear error messages for debugging
- Proper HTTP status codes

### 4. Configuration Management
- Environment variable support for sensitive data
- Spring Boot property-based configuration
- Debug logging for troubleshooting

## Setup & Configuration

### Prerequisites

- Java 11 or higher (tested with Java 26.0.1)
- Maven 3.6 or higher
- OpenAI API Key (from https://platform.openai.com/account/api-keys)

### Installation

1. **Clone/Extract the project**:
```bash
cd c:\workspace\langchain4jdemo
```

2. **Set OpenAI API Key** in `.vscode/launch.json`:
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Current File",
      "request": "launch",
      "mainClass": "com.example.demo.Langchain4jdemoApplication",
      "env": {
        "OPENAI_API_KEY": "sk-your-actual-api-key-here"
      }
    }
  ]
}
```

**Alternative**: Set as system environment variable:
```bash
$env:OPENAI_API_KEY="sk-your-actual-api-key-here"
```

3. **Build the project**:
```bash
.\mvnw.cmd clean install
```

4. **Run the application**:
```bash
.\mvnw.cmd spring-boot:run
```

The application will start on `http://localhost:8080`

## Running the Application

### Using Maven
```bash
cd c:\workspace\langchain4jdemo
.\mvnw.cmd spring-boot:run
```

### Using VS Code Debug
1. Press `F5` to start debugging
2. The application will launch with the configured OpenAI API key

### Using JAR
```bash
java -Dspring.profiles.active=prod -jar target/langchain4jdemo-0.0.1-SNAPSHOT.jar
```

## Testing the Endpoints

### Test with PowerShell
```powershell
# First request (allowed)
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/chat/hello" -Method GET
$response.Content

# Second request within 60 seconds (rate limited)
$response = Invoke-WebRequest -Uri "http://localhost:8080/api/chat/world" -Method GET
$response.Content
```

### Test with curl
```bash
# First request
curl -X GET "http://localhost:8080/api/chat/What is AI"

# Second request (will be rate limited)
curl -X GET "http://localhost:8080/api/chat/Hello"
```

## Configuration Files

### application.properties
Located at `src/main/resources/application.properties`

```properties
spring.application.name=langchain4jdemo

# OpenAI Configuration
langchain4j.open-ai.chat-model.api-key=${OPENAI_API_KEY}
langchain4j.open-ai.chat-model.model-name=gpt-4o-mini
langchain4j.open-ai.chat-model.temperature=0.7
langchain4j.open-ai.chat-model.max-tokens=50
```

### launch.json
Located at `.vscode/launch.json`

Debug configuration with environment variables for OpenAI API key.

## Troubleshooting

### Issue: "OPENAI_API_KEY environment variable is not set"
**Solution**: 
- Check that `OPENAI_API_KEY` is set in `launch.json` or system environment
- Restart the application after setting the variable

### Issue: "Incorrect API key provided"
**Solution**: 
- Verify the API key is valid at https://platform.openai.com/account/api-keys
- Ensure no extra spaces or characters in the key
- Regenerate a new API key if the current one is invalid

### Issue: "Port 8080 was already in use"
**Solution**:
```powershell
# Kill the process using port 8080
taskkill /F /IM java.exe

# Or configure a different port in application.properties
server.port=8081
```

### Issue: Rate limit exceeded immediately
**Solution**: 
- Wait 60 seconds for the counter to reset
- This is by design to prevent excessive API calls and costs

## Rate Limiting Details

- **Type**: Per-minute sliding window
- **Limit**: 1 request per 60 seconds
- **Storage**: In-memory atomic counters (thread-safe)
- **Scope**: Application-wide
- **Reset**: Automatic after 60 seconds

## System Message

The AI assistant uses the following system prompt:
> "You are a professional IT support assistant. Be polite and concise."

This guides the AI's responses to be helpful and professional for IT-related queries.

## Performance Notes

- **Max Tokens**: Limited to 50 tokens per response (configured in application.properties)
- **Temperature**: 0.7 (balanced between deterministic and creative)
- **Model**: GPT-4o-mini (fast, cost-effective)
- **Timeout**: Default OpenAI API timeout settings

## Security Considerations

- **API Key**: Never commit API keys to version control
- **Rate Limiting**: Prevents abuse and manages API costs
- **Error Messages**: Contain sensitive information in debug mode
- **Environment Variables**: Use for sensitive configuration

## Future Enhancements

- [ ] Database persistence for conversation history
- [ ] User authentication and session management
- [ ] Configurable rate limiting per user
- [ ] WebSocket support for real-time messaging
- [ ] Additional AI models support
- [ ] Streaming responses for long outputs
- [ ] Multi-language support

## License

This project is provided as-is for educational and demonstration purposes.

## Support

For issues or questions:
1. Check the [Troubleshooting](#troubleshooting) section
2. Review LangChain4j documentation: https://docs.langchain4j.dev/
3. Check OpenAI API documentation: https://platform.openai.com/docs/

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [LangChain4j Documentation](https://docs.langchain4j.dev/)
- [OpenAI API Reference](https://platform.openai.com/docs/api-reference)
- [Maven Documentation](https://maven.apache.org/guides/)
