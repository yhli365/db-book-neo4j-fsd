// Python
// $ pip install py2neo
// $ python p2n.py
from py2neo import Graph
graph = Graph(password="12345678")
print graph.data("MATCH (u:User{name:'user'}) RETURN u")