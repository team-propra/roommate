# Package Moved
changed by: themassiveone

## What
The package `dtos.forms` has been moved to `values.forms`.

## Why
Dtos and values are very similar constructs. Combining both into one directory reduces clutter.

Separation between domain values and html values is still maintained by the `values.domain` and `values.forms` packages.

Both subpackages are considered individual layers.