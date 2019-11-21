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
package test.duplicate.domainobject.name.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Customer {

    private CustomerId id;
    private List<Address> addressList;
    private Set<Address> addressSet;
    private Collection<Address> addressCollection;

    public CustomerId getId() {
        return id;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public Collection<Address> getAddressCollection() {
        return addressCollection;
    }

    public Set<Address> getAddressSet() {
        return addressSet;
    }
}
