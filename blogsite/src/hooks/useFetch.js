// import { useState, useEffect, useCallback } from "react";

// function useFetch(username, fetchFunc, page) {
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(false);
//   const [list, setList] = useState([]);

//   const sendQuery = useCallback(async () => {
//     try {
//       await setLoading(true);
//       await setError(false);
//       const res = await fetchFunc(username)
//       await setList((prev) => [...prev, ...res.data]);
//       setLoading(false);
//     } catch (err) {
//       setError(err);
//     }
//   }, [username, page]);

//   useEffect(() => {
//     sendQuery(username);
//   }, [username, sendQuery, page]);

//   return { loading, error, list };
// }

// export default useFetch;

import React, { useEffect, useState, useRef } from 'react'

const divStyle = {
  color: 'blue',
  height: '250px',
  textAlign: 'center',
  padding: '5px 10px',
  background: '#eee',
  marginTop: '15px',
}

const containerStyle = {
  maxWidth: '1280px',
  margin: '0 auto',
}

export function InfiniteScroll() {
  const [postList, setPostList] = useState({ list: [1, 2, 3, 4] })
  // tracking on which page we currently are
  const [page, setPage] = useState(1)
  // add loader reference
  const loader = useRef(null)
  // here we handle what happens when user scrolls to Load More div
  // in this case we just update page variable
  const handleObserver = (entities) => {
    const target = entities[0]
    if (target.isIntersecting) {
      setPage(_page => _page + 1)
    }
  }

  useEffect(() => {
    const options = {
      root: null,
      rootMargin: '20px',
      threshold: 1.0,
    }
    // initialize IntersectionObserver and attaching to Load More div
    const observer = new IntersectionObserver(handleObserver, options)
    if (loader.current) {
      observer.observe(loader.current)
    }
  }, [])

  useEffect(() => {
    // here we simulate adding new posts to List
    const newList = postList.list.concat([1, 1, 1, 1])
    setPostList({
      list: newList,
    })
  }, [page, postList.list])

  return (
    <div className="container" style={containerStyle}>
      <div className="post-list">
        {postList.list.map((post, index) => (
          <div key={index} className="post" style={divStyle}>
            <h2>{post}</h2>
          </div>
        ))}
        <div className="loading" ref={loader}>
          <h2>Load More</h2>
        </div>
      </div>
    </div>
  )
}
