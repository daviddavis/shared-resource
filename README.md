# shared-resource

Shared resource is a web application that facilitates the sharing of resources (e.g. conference rooms, software licenses, etc.).

## Usage

If you use cake, substitute 'lein' with 'cake' below. Everything should work fine.

```bash
git clone https://github.com/daviddavis/shared-resource.git
cd shared-resource
lein deps
cp resources/config.properties.example resources/config.properties
```
configure resources/config.properties for your Datomic database and LDAP server.
```bash
./create-db.sh
lein run
```

## Contributors

* [Mike Simpson](https://github.com/mjs2600) (mjs2600)
* [David Davis](https://github.com/daviddavis) (daviddavis)

## License

Copyright &copy; 2012 SciMed Solutions, Inc.

Distributed under the Eclipse Public License, the same as Clojure.

