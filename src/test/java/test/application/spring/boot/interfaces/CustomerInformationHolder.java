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
package test.application.spring.boot.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.application.spring.boot.model.Address;
import test.application.spring.boot.model.Customer;
import test.application.spring.boot.model.CustomerId;

@RestController
@RequestMapping({"/customers"})
public class CustomerInformationHolder {

    @PutMapping({"/{customerId}/address"})
    public ResponseEntity<Address> changeAddress(@PathVariable CustomerId customerId, @RequestBody Address requestDto) {
        // method will never be called; this is just for our reflection (scanning) tests;
        return null;
    }

    @GetMapping({"/{customerId}/"})
    public ResponseEntity<Customer> getCustomer(@PathVariable CustomerId customerId) {
        // method will never be called; this is just for our reflection (scanning) tests;
        return null;
    }

}
