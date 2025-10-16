/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.utilities.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestObserverLRU {
    @Test
    @DisplayName("test trimCache() to only notify subscribers when exceeding limit.")
    void testObserver() {
        ObserverLRU<String, String> lruObs = new ObserverLRU<String, String>(2);
        var isCalled = new AtomicBoolean(false);
        Consumer<String> l =
                (v) -> {
                    isCalled.set(true);
                };

        lruObs.subscribe(l);
        lruObs.update("foo", "bar");
        lruObs.update("deez", "nuts");

        assertFalse(isCalled.get());

        lruObs.update("say", "what");

        assertTrue(isCalled.get());

        lruObs.unsubscribe(l);
    }

    @Test
    @DisplayName(
            "test ObserverLRU to not cause indirect recursion when remove() is called by a subscriber")
    void testObserverIndirectRecursion() {
        ObserverLRU<String, String> lruObs = new ObserverLRU<String, String>(2);
        var calls = new AtomicInteger(0);
        Consumer<String> l2 =
                (s) -> {
                    // basically, if the remove() invocation inside this lambda function calls this
                    // subscriber lambda function, then calls != 0 by then. Verifies that the
                    // subscriber is only ever called once for the same key that's being removed
                    assertEquals(0, calls.getAndIncrement());
                    assertNotNull(s);
                    lruObs.remove("foo");
                };

        lruObs.update("foo", "bar");
        lruObs.update("deez", "nuts");
        lruObs.subscribe(l2);
        lruObs.remove("foo");
        lruObs.unsubscribe(l2);
    }
}
