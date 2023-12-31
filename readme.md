# CS-360 Project Three Portfolio Submission

## Briefly summarize teh requirements and goals of the app you developed. What user needs was this app designed to address?
The InventoryApp is a mobile application designed to help a users manage their inventory for their warehouse, online storefront or brick and mortor retail location. It allows for the adding and editing of inventory items, as well as low stock notifications via SMS. The InventoryApp also supports the creation of multiple user accounts.

## What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in Mind? Why were your designs successful?
The InventoryApp includes a login screen, inventory screen and settings screen. The login screen allows a user to login or register an account. The inventory screen lists available inventory with quantities, and includes familiar icons for editing and deleting inventory items. There is a floating action button that allows adding of invenotry items. Choosing to add or edit an inventory displays an additional screen to do so. The settings screen allows the user to enable SMS notiifcation of low inventory stock. The ability to load a dark theme will be included in a future enhancement. These UI elements work because they follow Android Design best practices and utilize icons already familiar to the user.

## How did you appraoch the process of coding your app? What techniques or strategies did you use? How could those be applied in the future?
When coding the app, I first started by researching what the user need is and what features would be critical to the application. After determining the user needs, I started to write out what screens would need to be included to meet the functional requirements of the user. Next I scetched out the screens to design the layout of the user interface. Once I started writing code, I broke the project into sections. I would write som small bit of fuctionality for one screen and test it. Then I would move on to the next one and then the next one.

## How did you test to ensure your code was functional? Why is this process important and what did it reveal?
When testing the functionality of the app, I would write out test cases with specific expected results. once my test cases were ready, I would test the specific function of the application with edge cases and document the results. This is important to know what happens when appropriate data is tested against, but also to find out the behavior against unexpected input.

## Considering the full app design and development process, from initial planning to finalization, where did you have to innovate to overcome a challange?
One of the requirements for the project was to design a login mechanism for the client. Since we were ustilizing SQLite databases stored on the device, I saw this as a potential security issue. To overcome this, I have utilized a hashing algorithm and unique salt for each user when they create their account. With this, the app is never storing the actual password of the user. So even if someone gets the SQLite database, they will not be able to easily obtain the passwords from it.

## In what specific component from your mobile app where you particularly successful in demonstrating?
I believed I demonstrated the Model-View-Contoller paradigm of application design vey well. I did this by utilizing a controller that controlled the business logic of the application. My views were the difent activity fragments that each served a single purpose. Creating DAOs for data retrieval and manipulation demonstrated the model portion of the MVC paradigm.
