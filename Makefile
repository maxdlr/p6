default: help

run: check-db ## Start project
	@make -j3 run-back run-front open-project

check-back: ## Build back
	cd back && mvn verify -DskipTests

run-back: check-back ## Start front
	cd back && mvn clean spring-boot:run

build-front: ## Build front
	cd front && npm install --no-audit

run-front: build-front ## Start back
	cd front && ng serve

check-db:
	@if [ ! -f .db-setup ]; then \
		echo "\033[1;31mThis project runs with a database.\033[0m"; \
		read -p "Have you set it up ? ([yes]/no): " response; \
		if [ "$$response" != "yes" ] && [ "$$response" != "" ]; then \
			echo "\033[1;31mPlease edit ./back/src/main/resources/application.properties file to match your setup. Then source ./back/create.sql and rerun this command.\033[0m"; \
			exit 1; \
		else \
			touch .db-setup; \
			make run; \
		fi \
	fi

open-project: ## Open project in browser
	xdg-open "http://localhost:4200" 2>/dev/null || open "http://localhost:4200" 2>/dev/null || start "http://localhost:4200" 2>/dev/null || echo "Could not open browser - Go to http://localhost:4200 -"

help: ## Show this help menu
	@awk -F ':|##' '/^[^\t].+?:.*?##/ {printf "\033[36m%-30s\033[0m %s\n", $$1, $$NF}' $(MAKEFILE_LIST)