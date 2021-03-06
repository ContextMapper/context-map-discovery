/*
 * Copyright 2019 The Context Mapper Project Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package test.duplicate.domainobject.name.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.duplicate.domainobject.name.model.Address;
import test.duplicate.domainobject.name.model.Customer;
import test.duplicate.domainobject.name.model.duplicate1.CustomerId;

@RestController
@RequestMapping({"customers", "second-ignored-mapping"})
public class CustomerInformationHolder {

    @PutMapping({"/{customerId}/address"})
    public ResponseEntity<Address> changeAddress(@PathVariable CustomerId customerId, @RequestBody Address requestDto) {
        // method will never be called; this is just for our reflection (scanning) tests;
        return null;
    }

    @PutMapping({"/{customerId}/address"})
    public ResponseEntity<Address> changeAddress(@PathVariable test.duplicate.domainobject.name.model.CustomerId customerId, @RequestBody Address requestDto) {
        // method will never be called; this is just for our reflection (scanning) tests;
        return null;
    }

    @GetMapping({"/{customerId}/"})
    public ResponseEntity<Customer> getCustomer(@PathVariable test.duplicate.domainobject.name.model.duplicate2.CustomerId customerId) {
        // method will never be called; this is just for our reflection (scanning) tests;
        return null;
    }

}
