# shared-resource

A website written in noir. 

## Usage

If you use cake, substitute 'lein' with 'cake' below. Everything should work fine.

```bash
git clone https://github.com/daviddavis/shared-resource.git
cd shared-resource
lein deps
cp -r resources/config.properties.example resources/config.properties
```
configure /resources/config.properties for your Datomic database and LDAP server.
```bash
./create-db.sh
lein run
```

## License

Copyright (C) 2012

Distributed under the Eclipse Public License, the same as Clojure.

