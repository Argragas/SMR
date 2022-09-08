# happi-explorer
[![CodeQL](https://github.com/Argragas/SMR/actions/workflows/codeql.yml/badge.svg?branch=main)](https://github.com/Argragas/SMR/actions/workflows/codeql.yml)
[![Sonar](https://github.com/Argragas/SMR/actions/workflows/sonar.yml/badge.svg?branch=main)](https://github.com/Argragas/SMR/actions/workflows/sonar.yml)

**H**yper **A**pplication **P**owerful **P**rogramming **I**nterface - **E**xplorer
A generic and dynamic endpoint for basic **CRUD** *(Create, Read, Update, Delete)* operation.  

## Showcase

For example, you want an endpoint for a `Customer` entity.  
This project provides full *CRUD* operation.  
So, you have dynamically this endpoint:  

~~~
GET     <your-path>/entity/customer
GET     <your-path>/entity/customser/{id}
POST    <your-path>/entity/customer
PUT     <your-path>/entity/customer/{id}
DELETE  <your-path>/entity/customer/{id}

OPTIONS <your-path>/entity/customer
OPTIONS <your-path>/entity/customer/{id}
~~~

### Filter

~~~
GET     <your-path>/entity/customer
~~~

This endpoint provides an advance filter query with a multiple operator.  
It's following this principe:  

~~~
attributName[operator]=value
~~~

* The attributName should be present in **DTO** *(Data Transfer Object)* class.  
* The logical operator should be in bracket.  
  * See `Operator` for more details.  
  * Logical operator `OR` can be used with **pipe** in value (e.g. `familyName[eq]=MINT|DOE`)
* The result is a pagination **JSON** *(JavaScrip Object Notation)*.  

Example:
~~~
GET     <your-path>/entity/customer?email[li]=linux.org&familyName[eq]=MINT
Content-Type: application/json

# Result:
{
  "data": [...],
  "size": 42,
  "pageSize": 10,
  "pageNumber": 1,
  "pageCount, 5
}
~~~

The `data` attribut contains *JSON* objects based on the query parameters.  
The result is the *JSON* object of the entity.  

### Find

~~~
GET     <your-path>/entity/customer/{id}
Content-Type: application/json

# Result:
{
 ...
}
~~~

This endpoint provides a simple find by unique identifier.  
By default, the identifier should be a **UUID** *(Universal Unique IDentifier)*.  

### Create

~~~
POST    <your-path>/entity/customer
Content-Type: application/json

# Body:
{
 ...
}

# Result:
201: Created /entity/{id}
~~~

This endpoint provides a simple created operation.  
The unique identifier attribut doesn't need to be defined.  
The *JSON* object of the entity is in the body of the request.  
The result contains the location of the new resource.  

### Update

~~~
PUT    <your-path>/entity/customer/{id}
Content-Type: application/json

# Body:
{
 ...
}

# Result:
204: No content
~~~

This endpoint provides a simple update operation.  
The unique identifier should be defined in the path parameter.  
The *JSON* object of the entity is in the body of the request.  
The result contains nothing.  

### Delete

~~~
DELETE    <your-path>/entity/customer/{id}
Content-Type: application/json

# Result:
204: No content
~~~

This endpoint provides a simple deleted operation.  
The unique identifier should be defined in the path parameter.  
The result contains nothing.  

## Setup

Example for a customer entry point: `Customer`.  
You need a *Jakarta 9.1* web project.  
See `happi-exeplorer-example` for more details.  

### Step 1: Create your table in a database

Create your table.  
See `src/glassfish/resources/derby-init.sql`.  

### Step 2: Model your class

Create your entity class using **JPA** *(Java Persistence API)*  
See `CustomerEntity` class.  

Create your data class using **JSON-B** *(JSON Binding)*  
See `CustomerDTO` class.  

Create your mapper class between entity and data class.  
See `CustomerMapper` class.  

> Simplify writing Java beans with [Lombok](https://projectlombok.org/).  
> You can generate mappers using [Mapstruct](https://mapstruct.org/).  

### Step 3: Register your entry point

Register your classes for publish your entry point.  
See `StartUp` class.  

~~~
@ApplicationScoped
public class StartUp {

    @Inject
    private ExplorerManager explorerManager;
    
    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {
        this.explorerManager.register(new DynamicEntry<>(
                "customer",
                Map.of(
                        Action.FILTER, HappiSecurityManager.PERMIT_ALL,
                        Action.FIND, HappiSecurityManager.PERMIT_ALL,
                        Action.CREATE, Roles.CUSTOMER_MANAGER,
                        Action.UPDATE, Roles.CUSTOMER_MANAGER,
                        Action.DELETE, Roles.CUSTOMER_MANAGER
                ),
                CustomerEntity.class,
                CustomerDTO.class,
                CustomerMapper.class,
                BasicExplorerService.class
        ));
    }
}
~~~

> **Note :**  
> Special roles :   
> `HappiSecurityManager.PUBLIC` : Full access, no authentication needed.  
> `HappiSecurityManager.PERMIT_ALL` : Full access for authenticated user, no role needed.  
> `HappiSecurityManager.DENY_ALL` : Unauthorized operation.  
> By default, if an action is not specified, the role is `HappiSecurityManager.DENY_ALL`.  
> **Semantic :** `role`, `group` and `permission` are synonym.  

## Extra features

### Manager endpoint

Enable manager endpoint feature in `StartUp` class :  

~~~
@ApplicationScoped
public class StartUp {

    @Inject
    private HappiSecurityManager securityManager;
    
    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {
      this.securityManager.putConfiguration(
                  HappiSecurityManager.Configuration.MANAGER_ENDPOINT, 
                  "true"
      );
    }
}
~~~

This endpoint will show all registered entries in `ExplorerManager` class.  

~~~
GET    <your-path>/manager/entry
Content-Type: application/json

# Result:
[
  {
    "path":"customer",
    "entity":"CustomerEntity",
    "data":"CustomerDTO",
    "mapper":"CustomerMapper",
    "service":"BasicExplorerService",
    "actions":[
      { "action":"UPDATE", "role":"customer-manager" },
      { "action":"DELETE", "role":"customer-manager" },
      { "action":"FIND",   "role":"@PermitAll" },
      { "action":"CREATE", "role":"customer-manager" },
      { "action":"FILTER", "role":"@PermitAll" }]
    }
]
~~~

Use `@DeclareRoles` annotation in your web configuration class:   

~~~
@DeclareRoles({ "customer-manager" })
@ApplicationPath("api")
public class WebConfiguration extends Application { }
~~~

And register this configuration class by `HappiSecurityManager` class in `StartUp` class:  

~~~
@ApplicationScoped
public class StartUp {

    @Inject
    private HappiSecurityManager securityManager;
    
    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {
      this.securityManager.scanRoleClassConfiguration(WebConfiguration.class);
    }
}
~~~

Now this endpoint will show all declared roles.

~~~
GET    <your-path>/manager/role
Content-Type: application/json

# Result:
[ "customer-manager" ]
~~~

## Discovery

Enable discovery endpoint feature in `StartUp` class :

~~~
@ApplicationScoped
public class StartUp {

    @Inject
    private HappiSecurityManager securityManager;
    
    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {
      this.securityManager.putConfiguration(
                  HappiSecurityManager.Configuration.DISCOVERY_ENDPOINT, 
                  "true"
      );
    }
}
~~~

Now this endpoint will show all declared data classes used by entry points.

~~~
GET    <your-path>/discovery
Content-Type: application/json

# Result:
[
  {
    "type": "CustomerDTO",
    "attributes": [
      {
        "name": "email",
        "type": "String",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.Email",
            "specifications": {
              "regexp": ".*",
              "flags": []
            }
          },
          {
            "name": "jakarta.validation.constraints.NotBlank",
            "specifications": {}
          },
          {
            "name": "jakarta.validation.constraints.Size",
            "specifications": {
              "min": 0,
              "max": 255
            }
          }
        ]
      },
      {
        "name": "gender",
        "type": "GenderDTO",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.NotNull",
            "specifications": {}
          }
        ]
      },
      {
        "name": "givenName",
        "type": "String",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.NotBlank",
            "specifications": {}
          },
          {
            "name": "jakarta.validation.constraints.Size",
            "specifications": {
              "min": 0,
              "max": 255
            }
          }
        ]
      },
      {
        "constraints": [],
        "name": "id",
        "type": "String"
      },
      {
        "name": "phoneNumber",
        "type": "String",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.NotBlank",
            "specifications": {}
          },
          {
            "name": "jakarta.validation.constraints.Size",
            "specifications": {
              "min": 0,
              "max": 255
            }
          }
        ]
      },
      {
        "name": "familyName",
        "type": "String",
        "constraints": [
          {
            "name": "jakarta.validation.constraints.NotBlank",
            "specifications": {}
          },
          {
            "name": "jakarta.validation.constraints.Size",
            "specifications": {
              "min": 0,
              "max": 255
            }
          }
        ]
      }
    ]
  }
]
~~~


___

> This project is writing in **Java 17** with **Jakarta 9.1**.  
> It uses **Glassfish 6.X** as standard implementation.  
> Build with **Maven 3.8.X**.  

