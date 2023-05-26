plugins {

}


dependencies {
    implementation(project(":common"))

}

allOpen {
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.Entity")
}



