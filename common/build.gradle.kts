plugins {

}

dependencies {

}

allOpen {
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.Entity")
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}
