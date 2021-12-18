package org.temkarus0070.orderpersistence.models;

import java.io.Serializable;

public enum Status implements Serializable {
    NEW,
    PENDING,
    PURCHASED,
    CANCELLED
}
