
## Code Example

### Suchen

Es gibt zwei typische Ansätze.
Einmal direkt über die Knoten:

~~~ { .java }
TreeSearch(x,k)
if x== NIL or k == x.key
    return x
if k < x.key
    return TreeSearch(x.left,k)
else
    return TreeSearch(x.right,k)
~~~~~~~~~~

Alternativ kann iterativ vorgegangen werden.

Da insbesondere das löschen iterativ gehandhabt werden muss
wird um die Konsistenz zu waren die iterative vorgehensweise animiert. 

~~~ { .java }
IterativeTreeSearch(x,k)
while x != NIL and k != x.key
    if k < x.key
        x = x.left
    else
        x = x.right
return x
~~~~~

### Einfügen

~~~ { .java }
TreeInsert(T,z)
y = NIL
x = T.root
while x != NIL
     y = x
     if z.key < x.key
          x = x.left
     else
        x = x.right
z.p = y
if y == NIL
     T.root = z
elseif z.key < y.key
     y.left = z
else
     y.right = z
~~~~~~~~~~~~~

### Entfernen

Beim Entfernen werden zwei Methoden in Kombination verwendet.

~~~ { .java }
Transplant(T,u,v)
if u.p == NIL
    T.root = v
elseif u == u.p.left
    u.p.left = v
else
    u.p.right = v
if v != NIL
    v.p = u.p
~~~~

~~~ { .java }
TreeDelete(T,z)
if z.left == NIL
    Transplant(T,z,z.right)
elseif z.right == NIL
    Transplant(T,z,z.left)
else
    y = TreeMinimum(z.right)
    if y.p != z
        Transplant(T,y,y.right)
        y.right = z.right
        y.right.p = y
    Transplant(T,z,y)
    y.left = z.left
    y.left.p = y
~~~~

