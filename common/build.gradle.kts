plugins {

}

dependencies {
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

allOpen {
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.Entity")
}

tasks {
    jar {
        enabled = false
    }
}