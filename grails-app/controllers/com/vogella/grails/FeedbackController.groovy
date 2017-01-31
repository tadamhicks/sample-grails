package com.vogella.grails

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class FeedbackController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    /*
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Feedback.list(params), model:[feedbackCount: Feedback.count()]
    }
    */

    def scaffold = Feedback

    def show(Feedback feedback) {
        respond feedback
    }

    def create() {
        respond new Feedback(params)
    }

    @Transactional
    def save(Feedback feedback) {
        if (feedback == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feedback.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feedback.errors, view:'create'
            return
        }

        feedback.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'feedback.label', default: 'Feedback'), feedback.id])
                redirect feedback
            }
            '*' { respond feedback, [status: CREATED] }
        }
    }

    def edit(Feedback feedback) {
        respond feedback
    }

    @Transactional
    def update(Feedback feedback) {
        if (feedback == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feedback.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feedback.errors, view:'edit'
            return
        }

        feedback.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'feedback.label', default: 'Feedback'), feedback.id])
                redirect feedback
            }
            '*'{ respond feedback, [status: OK] }
        }
    }

    @Transactional
    def delete(Feedback feedback) {

        if (feedback == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        feedback.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'feedback.label', default: 'Feedback'), feedback.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'feedback.label', default: 'Feedback'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
