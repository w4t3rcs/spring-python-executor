from py4j.java_gateway import JavaGateway

gateway = JavaGateway()
entry_point = gateway.entry_point

print(entry_point)
