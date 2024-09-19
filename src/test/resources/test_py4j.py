from py4j.java_gateway import JavaGateway

gateway = JavaGateway()
entry_point = gateway.entry_point
elements = gateway.getElements()
element = elements.get(0)
print(entry_point)
print(element)