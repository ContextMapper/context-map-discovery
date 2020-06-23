# Publication Management Example (from ICWE 2020 keynote)
This example illustrates the OpenAPI discovery strategy by using the following demo from ICWE 2020:
[https://ozimmer.ch/practices/2020/06/10/ICWEKeynoteAndDemo.html](https://ozimmer.ch/practices/2020/06/10/ICWEKeynoteAndDemo.html)

**Note**: The OpenAPI discovery strategy is not yet released! (this is in proof-of-concept state only)

## The OpenAPI Specification (Input)
The [input file](./src/main/resources/specification.yml) of this example has been taken from the [blogpost](https://ozimmer.ch/practices/2020/06/10/ICWEKeynoteAndDemo.html) 
mentioned above and was generated with Context Mapper. With this example we discover the corresponding Bounded Context again and compare the result with the original CML input.

## The Original CML Model
This is the CML model from which the OpenAPI specification (OAS) has been generated:

```
BoundedContext ReferenceManagementContext implements PaperArchive {
	domainVisionStatement "This Bounded Context realizes the following subdomains: PaperArchive"
	type APPLICATION
	/* This Aggregate contains the entities and services of the 'PaperArchive' subdomain.
	 * TODO: You can now refactor the Aggregate, for example by using the 'Split Aggregate by Entities' architectural refactoring.
	 * [x] TODO: Add attributes and operations to the entities.
	 * [x] TODO: Add operations to the services.
	 * Find examples and further instructions on our website: https://contextmapper.org/docs/rapid-ooad/ */
	Aggregate PaperArchiveAggregate {
		Service PaperArchivingService {
			@PaperItem createPaperItem (String who, String what, String where); // corrected
			Set<@PaperItem>lookupPapersFromAuthor(String who); // added 
			String convertToMarkdownForWebsite(@PaperItemID id); // added 
		}
		Entity PaperItem {
			String title
			String authors
			String venue
			-PaperItemID paperItemId
		}
		Entity PaperCollection {
			int paperCollectionId // corrected
			- List<PaperItem> paperItemList
		}
		ValueObject PaperItemID { // added
			String doi
		}
	}
}
```

## The Discovery Code (Strategy not yet released!)
The discovered CML model is generated with the following code: (can also be found in `src/main/java` folder):

```java
public class PublicationManagementDiscovery {

  public static void main(String[] args) throws IOException {
    ContextMapDiscoverer discoverer = new ContextMapDiscoverer()
        .usingBoundedContextDiscoveryStrategies(
            new OASBoundedContextDiscoveryStrategy("./src/main/resources/specification.yml"));

    ContextMap contextMap = discoverer.discoverContextMap();
    new ContextMapSerializer().serializeContextMap(contextMap, new File("./src-gen/publication-management.cml"));
  }

}
```

## The Resulting CML Bounded Context
The code above generates the following CML model: (can also be found [here](./src-gen/publication-management.cml))

```
ContextMap {
	contains ReferenceManagementServiceAPI

}

BoundedContext ReferenceManagementServiceAPI {
	/* general data-oriented endpoint */
	Aggregate PaperArchiveFacade {
		/* This service contains all operations of the following endpoint: /PaperArchiveFacade */
		Service PaperArchiveFacadeService {
			@PaperItemDTO createPaperItem (@CreatePaperItemParameter input);
			@ConvertToMarkdownForWebsiteReturnType convertToMarkdownForWebsite (@PaperItemKey input);
			List<@PaperItemDTO> lookupPapersFromAuthor (String Parameter1);
		}
		Entity PaperItemDTO {
			String title
			String venue
			String authors
			- PaperItemIdType paperItemId
		}
		Entity PaperItemKey {
			String doi
		}
		Entity PaperItemIdType {
			String doi
		}
		Entity CreatePaperItemParameter {
			String where
			String what
			String who
		}
		Entity ConvertToMarkdownForWebsiteReturnType {
			String anonymous1
		}
	}
}
```
