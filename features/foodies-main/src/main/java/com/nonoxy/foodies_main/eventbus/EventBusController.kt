package com.nonoxy.foodies_main.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBusController {

    private val _eventBus = MutableSharedFlow<EventBus>(replay = 1)
    val eventBus = _eventBus.asSharedFlow()

    suspend fun publishEvent(event: EventBus) {
        _eventBus.emit(event)
    }
}