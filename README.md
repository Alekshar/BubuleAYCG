# Algorithme du projet

L'algorithme fonctionne de la manière suivante :

Pour chaque point il va chercher tout les voisins dont la distances avec le point est inférieur à un seuil de distance maximum.
Chaque point possède maintenant une liste de voisin.

On parcourt tout les points P1, on regarde leurs voisins P2, 

on test les voisins P3 de P2 en vérifiant leurs validités (distances P1P2 = distance P2P3 avec une marge d'erreur, angles < maxAngle)

si ils sont valides on test les voisins de P3 pour trouver les potentiels points P4 d'une série, 

enfin de même pour la validité d'un point P5 voisin de P4.

Si le point P5 vérifie les conditions de validités, une série a été trouvé, les points ne sont donc plus réutilisables.

Une fois qu'une recherche est effectué, on ré-éffectue une recherche avec des critères moins strictes, puis des critères encore moins strictes.

Enfin on repart de 0 en mettant à jour la liste de voisin des points avec un seuil de distance maximum plus important, puis encore plus important ...
