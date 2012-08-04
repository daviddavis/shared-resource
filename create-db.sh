project_classpath=`lein classpath`
echo $project_classpath
java -cp $project_classpath  clojure.main -i ./src/shared_resource/datomic.clj -e "(use 'shared-resource.datomic) (create-database) (load-schema) (shutdown-agents)"
