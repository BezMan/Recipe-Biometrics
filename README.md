# Recipe-Biometrics

## please switch to RELEASE flavor if you want to do network call. (DEBUG flavor is using mock json data for future testing purposes)

### key points:

- Developed app with scalable architecture and package structure. 
- Retrofit as Http client
- using Compose Navigation between screens passing recipe id.
- mutual VM for both screens, as they share the same repo.
- added Hilt DI, swaping repositories according to flavor.
- added mock json for DEBUG flavor, instead of doing actual network call, for easy testing. 
- encryption code would not be exposed in production.
- handled case where no biometrics are found or configured.
