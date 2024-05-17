# TODO

- [ ] Clean up sql file
    - [ ] inserts
    - [ ] foreign keys
- [ ] Javadocs
- [x] Validation
  - [x] Controllers
  - [x] Models
- [x] Test user and role endpoints
- [x] ~~Ask about JSON structure requirement~~ Created JSON structures for all endpoints
- [x] Fix multiple roles in controllers (@PreAuthorize('hasAuthority('DOCTOR', 'ADMIN')') -> @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')"))