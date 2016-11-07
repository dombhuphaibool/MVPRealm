# MVPRealm
Example project of refactoring an Activity into MVP components

The original Activity, named OriginalActivity which contains a RecyclerView and an Adapter is refactored to use an MVP pattern. OriginalActivity is broken into MVPActivity implementing PeopleView, a PeoplePresenter containing the buisness logic, and a PeopleAdapter which creates an implementation of PersonView.

The presenter does not have any knowledge of Android components. The presenter knows about models and uses a new DataManager to retrieve them. The DataManager hides away network access through the NetworkManager. The idea is that the DataManager will check local persistent storage before going to the network to request data.

With the MVP, pattern we can write robust unit tests for our presenter. We can also provide multiple views for the same presenter. View logic is no longer intertwined with buisness logic. Separating the DataManager and encapsulating the NetworkManager, the presenter doesn't care where the model comes from and its logic is not intertwined with networking code.
