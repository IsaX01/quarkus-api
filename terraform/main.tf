provider "google" {
    project = var.project
    region = var.region
    zone = var.zone
}

resource "google_container_cluster" "primary" {
  name     = "cluster-1"
  location = var.zone
  deletion_protection = false
  network  = "projects/${var.project}/global/networks/default"


  node_config {
    machine_type = "e2-micro"
  }

  lifecycle {
    ignore_changes = [
      # Atributos y bloques a ignorar
      addons_config,
      master_auth,
      node_config,
      node_pool,
      # Otros atributos si es necesario
    ]
  }
}

data "google_client_config" "default" {}

# Configurar el proveedor de Kubernetes
provider "kubernetes" {
  host                   = google_container_cluster.primary.endpoint
  token                  = data.google_client_config.default.access_token
  cluster_ca_certificate = base64decode(google_container_cluster.primary.master_auth.0.cluster_ca_certificate)
}

# Desplegar la aplicación en Kubernetes
resource "kubernetes_deployment" "app_quarkus" {
  metadata {
    name = "app-deployment-quakus"
  }
  spec {
    replicas = 2
    selector {
      match_labels = {
        app = "my-app-quakus"
      }
    }
    template {
      metadata {
        labels = {
          app = "my-app-quakus"
        }
      }
      spec {
        container {
          image = "gcr.io/${var.project}/app:latest"
          name  = "app-container-quakus"
          port {
            container_port = 8000
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "app_service_quarkus" {
  metadata {
    name = "app-service-quakus"
  }
  spec {
    selector = {
      app = "my-app-quakus"
    }
    type = "LoadBalancer"
    port {
      port        = 80
      target_port = 8000
    }
  }
}

terraform {
  backend "gcs" {
    bucket  = "terraform-state-isax01"
    prefix  = "terraform/state"
  }
}

resource "google_sql_database_instance" "db_instance" {
  name             = "isax01"
  database_version = "POSTGRES_12"
  region           = var.region
  edition          = "ENTERPRISE_PLUS"

  lifecycle {
    prevent_destroy = true
  }

  settings {
    tier = "db-custom-2-7680"
  }
}

resource "google_sql_database" "db" {
  name     = "event_managements"
  instance = google_sql_database_instance.db_instance.name
}

resource "google_sql_user" "users" {
  name     = "isax01"
  instance = google_sql_database_instance.db_instance.name
  password = var.db_password
}