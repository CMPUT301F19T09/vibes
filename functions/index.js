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

exports.searchUsers = functions.https.onCall((data, context) =>
{
    let search = data.search.toLowerCase();
    let users = db.collection('users').orderBy('first');
    let uids = [];
    return users.get().then(snapshot =>
        {
            snapshot.forEach(doc =>
                {
                    let user = doc.data();
                    let username = user.username.toLowerCase();
                    let firstname = user.first.toLowerCase();
                    let lastname = user.last.toLowerCase();
                    let fullname = firstname + lastname;
                    let fullnamespace = firstname + ' ' + lastname;


                    if (username.includes(search) ||
                        firstname.includes(search) ||
                        lastname.includes(search) ||
                        fullname.includes(search) ||
                        fullnamespace.includes(search))
                    {
                        console.log('user ' + doc.id + ' => ' + username);
                        uids.push(doc.id);
                    }
                });

            return uids;
        });
});

exports.createUser = functions.https.onCall((data, context) =>
{
});

exports.deleteUser = functions.https.onCall((data, context) =>
{
    let uid = data.uid;

    let users = db.collection('users');
    let user = users.doc(uid);
    users.get().then(snapshot =>
    {
        snapshot.forEach(doc =>
        {
            let other_user = doc.data();

            if (other_user.following_list
        }
    });
});
