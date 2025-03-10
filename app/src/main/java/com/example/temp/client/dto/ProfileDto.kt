package com.example.temp.client.dto

data class ProfileDto (
    /* Public data */
    var publicName: String,
    var publicBio: String?,
    var publicAgeGroup: String?, // Young, Adult, Old
    var publicGender: String?, // Male, Female

    /* Private data */
    var emailAddress: String?
)