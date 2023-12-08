# Package moved
changed by: themassiveone

## What
The package `domain.models.values` has been moved to `values.domain`.

## Why
The package `domain.models.values` was located inside `domain.models`. 
By onion definition, this results in domain values not being accessible outside the `domain.models`/`domain.services` package.

Unfortunately values like `ItenName` are supposed to be accessible outside the domain, e.g. in the `controllers` package.

We could have defined our own value in order to pass it into other layers and map between those two. 
We decided not to do this, because it would have been a lot of boilerplate code for no real benefit.

