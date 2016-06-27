import {Injectable} from 'angular2/core'

@Injectable()
export class TestSuitesChangedEvent{
    
    listeners: ListenerWithContext[] = [];

    subscribe(callback: () => void, context: any) {
        this.listeners.push(new ListenerWithContext(callback, context));
    }

    notify() {
        for(let listener of this.listeners) {
            listener.callback.call(listener.context);
        }
    }
}

class ListenerWithContext {
    constructor(public callback: () => void, public context: any) {}
}