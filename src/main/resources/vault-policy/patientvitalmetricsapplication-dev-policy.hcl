# patientvitalmetricsapplication-policy.hcl
path "secret/patientvitalmetricsapplication/dev" {
  capabilities = ["create", "read", "update", "delete", "list"]
}
path "secret/data/patientvitalmetricsapplication/dev" {
  capabilities = ["read"]
}

path "secret/data/patientvitalmetricsapplication" {
  capabilities = ["read"]
}

path "secret/metadata/patientvitalmetricsapplication/dev" {
  capabilities = ["read", "list"]
}
