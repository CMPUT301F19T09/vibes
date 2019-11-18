const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

let db = admin.firestore();

exports.getMostRecentMoodEvent = functions.https.onCall((data, context) =>
    {
        const uid = data.uid;
        const filters = data.filters;

        if (uid == null)
        {
            console.log('tried to access most recent mood event of null user');
        }

        let user = db.collection('users').doc(uid);

        return user.get().then((snapshot) =>
            {
                if (!snapshot.exists)
                {
                    console.log('this user doesn\'t exist');
                    return null;
                }
                else
                {
                    let result = null;
                    if (filters == null || filters[0] == '')
                    {
                        return snapshot.data().moods[0];
                    }
                    for (const mood of snapshot.data().moods)
                    {
                        if (filters.includes(mood.emotion))
                        {
                            result = mood;
                            break;
                        }
                    }
                    return result;
                }
            })
            .catch((error) =>
                {
                    console.log('error occurred: ' + error);
                    return null;
                });
    });

exports.sortMoodEvents = functions.firestore
    .document('users/{uid}')
    .onWrite((change, context) =>
    {
        let user = db.collection('users').doc(context.params.uid);
    });
