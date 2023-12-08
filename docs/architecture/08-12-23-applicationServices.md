# Package Moved
changed by: themassiveone

## What
The package `applicationServices` has been moved to `application.services`.

## Why
The package `application.data` was just added due to application services being required to create entity interface objects.
Both application services and application data are now combined in the same package: `application`. 

This is good, as they belong together and are also considered the same _onion layer_