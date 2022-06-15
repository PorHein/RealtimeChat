package com.example.realtimechat

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T>: MutableLiveData<T>() {

    private var isPending : AtomicBoolean = AtomicBoolean(false)

    //Thread A
    //var a =1
    //a + 1 =2
    //a + 2 =3
    //Flaky
    // use AtomicBoolean to prevent this
    //Thread B
    //a = 2

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, { value ->
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        })
    }

    override fun setValue(value: T) {
        isPending.set(true)
        super.setValue(value)
    }
}
