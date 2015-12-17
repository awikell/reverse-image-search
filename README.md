# reverse-image-search

This is a sample application that demonstrates the strength of reverse image searching (finding similar images based on another image as input). It was created during the Schibsted Hackday 15 (https://www.facebook.com/SchibstedHackday/).


# Running it

The application is based on Spring Boot but the build step is not properly setup to create a runnable standalone jar so for now 
I recommend to run it from inside your IDEA. Steps are:

1. Make sure that the JAR's in the `lib/` folder are added to your classpath.
2. Run it as a regular java application with the main class as `app.Application`

By default the app will be available on `http://localhost:8080`


# Libraries used

The reverse image searching uses the LIRE java library (https://github.com/dermotte/LIRE).
It has support for indexing images in a Lucene index and then
performing searches on the index using several different algorithms.
