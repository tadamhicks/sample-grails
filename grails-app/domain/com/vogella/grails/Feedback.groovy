package com.vogella.grails

class Feedback {
    String title
    String feedback
    Date dateCreated
    Date lastUpdated

    User user
    static hasMany = [comments: Comment]

    static constraints = {
        title(blank:false, nullable: false, size:3..80)
        feedback(blank:false, nullable: false, size: 3..500)
        user(nullable: false)
    }
}
