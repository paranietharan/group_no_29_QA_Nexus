# Configuration Guide

Configuration file: `src/test/resources/config.properties`

| Property | Default | Description |
|----------|---------|-------------|
| `api.baseUrl` | `http://localhost:8080` | API base URL |
| `api.auth.loginPath` | `/api/auth/login` | Login endpoint |
| `ui.baseUrl` | `http://localhost:8080` | UI base URL |
| `ui.loginPath` | `/ui/login` | Login page path |
| `browser` | `chrome` | `chrome`, `firefox`, `edge` |
| `browser.headless` | `false` | Headless mode |
| `browser.window.width` | `1920` | Window width |
| `browser.window.height` | `1080` | Window height |
| `timeout.implicit` | `10` | Implicit wait (sec) |
| `timeout.explicit` | `10` | Explicit wait (sec) |
| `timeout.page.load` | `30` | Page load (sec) |

## Command Line Override

```sh
mvn test -Dbrowser=firefox -Dbrowser.headless=true
```
