# Detect docker compose command (either `docker compose` or legacy `docker-compose`)
DOCKER_COMPOSE := $(shell command -v "docker compose" >/dev/null 2>&1 && echo "docker compose" || echo "docker-compose")

# Compose files
COMPOSE_FILES := -f docker-compose.yaml
COMPOSE_FILES_OBS := -f docker-compose.yaml -f docker-compose.observability.yaml

# Default target
.DEFAULT_GOAL := help

# Help target
.PHONY: help
help:
	@echo ""
	@echo "📦 OFDS Project - Makefile Commands"
	@echo "-----------------------------------"
	@echo "make start-all     🔧 Start all services including observability stack"
	@echo "make start-apps    🚀 Start only application services"
	@echo "make bootstrap     ⚙️  Run initial bootstrap script"
	@echo "make teardown      🧹 Stop all running services and clean up"
	@echo "make help          📝 Show this help message"
	@echo ""

# Target: Start everything including observability
.PHONY: start-all
start-all:
	@echo "🔧 Starting all services (including observability)..."
	@$(DOCKER_COMPOSE) $(COMPOSE_FILES_OBS) up --build -d

# Target: Start only the core application services
.PHONY: start-apps
start-apps:
	@echo "🚀 Starting application services only..."
	@$(DOCKER_COMPOSE) $(COMPOSE_FILES) up --build -d

# Target: Bootstrap setup
.PHONY: bootstrap
bootstrap:
	@echo "⚙️ Running bootstrap script..."
	@bash scripts/bootstrap.sh

# Target: Teardown all containers
.PHONY: teardown
teardown:
	@echo "🧹 Tearing down all services..."
	@$(DOCKER_COMPOSE) $(COMPOSE_FILES_OBS) down

