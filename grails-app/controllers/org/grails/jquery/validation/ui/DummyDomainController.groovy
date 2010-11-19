package org.grails.jquery.validation.ui

class DummyDomainController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [dummyDomainInstanceList: DummyDomain.list(params), dummyDomainInstanceTotal: DummyDomain.count()]
    }

    def create = {
        def dummyDomainInstance = new DummyDomain()
        dummyDomainInstance.properties = params
        return [dummyDomainInstance: dummyDomainInstance]
    }

    def save = {
        def dummyDomainInstance = new DummyDomain(params)
        if (dummyDomainInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'dummyDomain.label', default: 'DummyDomain'), dummyDomainInstance.id])}"
            redirect(action: "show", id: dummyDomainInstance.id)
        }
        else {
            render(view: "create", model: [dummyDomainInstance: dummyDomainInstance])
        }
    }

    def show = {
        def dummyDomainInstance = DummyDomain.get(params.id)
        if (!dummyDomainInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dummyDomain.label', default: 'DummyDomain'), params.id])}"
            redirect(action: "list")
        }
        else {
            [dummyDomainInstance: dummyDomainInstance]
        }
    }

    def edit = {
        def dummyDomainInstance = DummyDomain.get(params.id)
        if (!dummyDomainInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dummyDomain.label', default: 'DummyDomain'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [dummyDomainInstance: dummyDomainInstance]
        }
    }

    def update = {
        def dummyDomainInstance = DummyDomain.get(params.id)
        if (dummyDomainInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (dummyDomainInstance.version > version) {
                    
                    dummyDomainInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'dummyDomain.label', default: 'DummyDomain')] as Object[], "Another user has updated this DummyDomain while you were editing")
                    render(view: "edit", model: [dummyDomainInstance: dummyDomainInstance])
                    return
                }
            }
            dummyDomainInstance.properties = params
            if (!dummyDomainInstance.hasErrors() && dummyDomainInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'dummyDomain.label', default: 'DummyDomain'), dummyDomainInstance.id])}"
                redirect(action: "show", id: dummyDomainInstance.id)
            }
            else {
                render(view: "edit", model: [dummyDomainInstance: dummyDomainInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dummyDomain.label', default: 'DummyDomain'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def dummyDomainInstance = DummyDomain.get(params.id)
        if (dummyDomainInstance) {
            try {
                dummyDomainInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'dummyDomain.label', default: 'DummyDomain'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'dummyDomain.label', default: 'DummyDomain'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'dummyDomain.label', default: 'DummyDomain'), params.id])}"
            redirect(action: "list")
        }
    }
}
