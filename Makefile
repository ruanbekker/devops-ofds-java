# Detect docker compose CLI variant
ifeq (, $(shell which docker-compose 2>/dev/null))
  ifeq (, $(shell docker compose version 2>/dev/null))
    $(error ❌ docker-compose or docker compose is required but not found)
  endif
  DC := docker compose
else
  DC := docker-compose
endif

# Compose files
COMPOSE_FILES := -f docker-compose.yaml
COMPOSE_FILES_OBS := -f docker-compose.yaml -f docker-compose.observability.yaml

.DEFAULT_GOAL := help

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

.PHONY: start-all
start-all:
	@echo "🔧 Starting all services (including observability)..."
	@$(DC) $(COMPOSE_FILES_OBS) up --build -d

.PHONY: start-apps
start-apps:
	@echo "🚀 Starting application services only..."
	@$(DC) $(COMPOSE_FILES) up --build -d

.PHONY: bootstrap
bootstrap:
	@echo "⚙️  Running bootstrap script..."
	@bash scripts/bootstrap.sh

.PHONY: teardown
teardown:
	@echo "🧹 Tearing down all services..."
	@$(DC) $(COMPOSE_FILES_OBS) down

